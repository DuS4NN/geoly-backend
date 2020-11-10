package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Point;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.models.StatusMessage;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class AdminSeasonService {

    private EntityManager entityManager;
    private DSLContext create;

    public AdminSeasonService(EntityManager entityManager, DSLContext create) {
        this.entityManager = entityManager;
        this.create = create;
    }

    public Response getSeason(int page){

        Select<?> query =
            create.select(DSL.year(Point.POINT.CREATED_AT), DSL.month(Point.POINT.CREATED_AT))
                .from(Point.POINT)
                .groupBy(DSL.concat(DSL.year(Point.POINT.CREATED_AT), DSL.month(Point.POINT.CREATED_AT)))
                .orderBy(DSL.year(Point.POINT.CREATED_AT).desc(), DSL.month(Point.POINT.CREATED_AT).desc())
                .limit(20)
                .offset((page-1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public int getSeasonCount(){
        Select<?> query =
                create.select(DSL.count())
                    .from(
                        create.select()
                            .from(Point.POINT)
                            .groupBy(DSL.concat(DSL.year(Point.POINT.CREATED_AT), DSL.month(Point.POINT.CREATED_AT))));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        Object result = q.getSingleResult();

        return Integer.parseInt(String.valueOf(result));
    }

    public Response getSeasonDetails (int page, int year, int month){

        Select<?> query =
            create.select(DSL.sum(Point.POINT.AMOUNT), User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, User.USER.ID)
                .from(Point.POINT)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(Point.POINT.USER_ID))
                .where(DSL.year(Point.POINT.CREATED_AT).eq(year))
                .and(DSL.month(Point.POINT.CREATED_AT).eq(month))
                .groupBy(Point.POINT.USER_ID)
                .orderBy(DSL.sum(Point.POINT.AMOUNT).desc())
                .limit(20)
                .offset((page-1)*20);
        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public int getSeasonDetailsCount(int year, int month){

        Select<?> query =
            create.select(DSL.count())
                .from(
                    create.select()
                        .from(Point.POINT)
                        .where(DSL.year(Point.POINT.CREATED_AT).eq(year))
                        .and(DSL.month(Point.POINT.CREATED_AT).eq(month))
                        .groupBy(Point.POINT.USER_ID));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        Object result = q.getSingleResult();

        return Integer.parseInt(String.valueOf(result));
    }
}
