package com.geoly.app.config.AuthenticationHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.UserOption;
import com.geoly.app.repositories.UserOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserOptionRepository userOptionRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.ACCEPTED.value());
        Map<String, Object> data = new HashMap<>();

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Optional<UserOption> userOption = userOptionRepository.findByUser(customUserDetails.getUser());
        if(!userOption.isPresent()) return;

        data.put("nickName", customUserDetails.getUser().getNickName());
        data.put("profileImage", customUserDetails.getUser().getProfileImageUrl());
        data.put("mapTheme", userOption.get().getMapTheme());
        data.put("darkMode", userOption.get().isDarkMode());
        data.put("languageId", userOption.get().getLanguage().getId());
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }
}
