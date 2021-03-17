package com.geoly.app.config.AuthenticationHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geoly.app.config.API;
import com.geoly.app.jooq.tables.Role;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.jooq.tables.UserOption;
import com.geoly.app.jooq.tables.UserRole;
import com.geoly.app.models.CustomUserDetails;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DSLContext create;

    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.ACCEPTED.value());
        Map<String, Object> data = new HashMap<>();

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Select<?> roles =
            create.select(Role.ROLE.NAME)
                .from(UserRole.USER_ROLE)
                .leftJoin(Role.ROLE)
                    .on(Role.ROLE.ID.eq(UserRole.USER_ROLE.ROLE_ID))
                .where(UserRole.USER_ROLE.USER_ID.eq(customUserDetails.getUser().getId()));

        Query q1 = entityManager.createNativeQuery(roles.getSQL());
        API.setBindParameterValues(q1, roles);
        List rolesResult = q1.getResultList();

        Select<?> options =
            create.select(UserOption.USER_OPTION.LANGUAGE_ID, UserOption.USER_OPTION.MAP_THEME, UserOption.USER_OPTION.DARK_MODE, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, User.USER.ADDRESS, User.USER.ADDRESS_UPDATE, User.USER.ID)
                .from(UserOption.USER_OPTION)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(UserOption.USER_OPTION.USER_ID))
                .where(UserOption.USER_OPTION.USER_ID.eq(customUserDetails.getUser().getId()));

        Query q2 = entityManager.createNativeQuery(options.getSQL());
        API.setBindParameterValues(q2, options);
        List optionsResult = q2.getResultList();

        data.put("roles", rolesResult);
        data.put("options", optionsResult);

        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(objectMapper.writeValueAsString(data));
    }
}
