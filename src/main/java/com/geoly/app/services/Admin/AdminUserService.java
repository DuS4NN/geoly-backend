package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.repositories.UserRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class AdminUserService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;

    public AdminUserService(EntityManager entityManager, DSLContext create, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
    }

    public Response getUsers(String nick , int page){

        Condition condition = DSL.trueCondition();
        if(!nick.equals("")){
            condition = condition.and(User.USER.NICK_NAME.like("%"+nick+"%"));
        }

        Select<?> query =
            create.select(User.USER.ID, User.USER.NICK_NAME, User.USER.CREATED_AT, User.USER.PROFILE_IMAGE_URL)
                .from(User.USER)
                .where(condition)
                .orderBy(User.USER.CREATED_AT.desc())
                .limit(20)
                .offset((page - 1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List response = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, response);
    }

    public long getUserCounts(){
        return userRepository.count();
    }
}
