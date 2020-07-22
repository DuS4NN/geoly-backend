package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.Point;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.models.StatusMessage;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

import static org.jooq.impl.DSL.*;

@Service
public class RankService {

    private EntityManager entityManager;
    private DSLContext create;

    public RankService(EntityManager entityManager, DSLContext create){
        this.entityManager = entityManager;
        this.create = create;
    }

    public List getTopPlayers(){
        Select<?> query =
            create.select(sum(Point.POINT.AMOUNT), User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL)
            .from(Point.POINT)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Point.POINT.USER_ID))
            .where(month(Point.POINT.CREATED_AT).eq(month(currentDate())))
            .groupBy(Point.POINT.USER_ID)
            .orderBy(sum(Point.POINT.AMOUNT).desc())
            .limit(50);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.TOP_PLAYERS_NOT_FOUND, HttpStatus.NO_CONTENT));
        return result;
    }

    public List getPlayer(int userId){
        Table<?> rank =
            create.select(Point.POINT.USER_ID.as("userId"))
            .from(Point.POINT)
            .where(month(Point.POINT.CREATED_AT).eq(month(currentDate())))
            .groupBy(Point.POINT.USER_ID)
            .orderBy(sum(Point.POINT.AMOUNT).desc())
            .asTable("rank");

        Table<?> user =
            create.select(sum(Point.POINT.AMOUNT).as("sum"),User.USER.PROFILE_IMAGE_URL.as("profile_img"), User.USER.NICK_NAME.as("nick_name"))
            .from(Point.POINT)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Point.POINT.USER_ID))
            .where(month(Point.POINT.CREATED_AT).eq(month(currentDate())))
            .and(Point.POINT.USER_ID.eq(userId))
            .groupBy(Point.POINT.USER_ID)
            .asTable("user");

        Select<?> query =
            create.select(count(), user.field("sum"), user.field("nick_name"), user.field("profile_img"))
            .from(rank, user)
            .where(field("userId").greaterOrEqual(userId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        Object[] obj = (Object[]) result.get(0);
        if(Integer.parseInt(String.valueOf(obj[0])) <= 2){
            return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_ALREADY_IN_TOP, HttpStatus.CONFLICT));
        }

        return result;
    }
}
