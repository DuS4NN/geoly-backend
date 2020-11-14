package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Log;
import com.geoly.app.models.LogType;
import com.geoly.app.models.StatusMessage;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminLogService {

    private EntityManager entityManager;
    private DSLContext create;

    public AdminLogService(EntityManager entityManager, DSLContext create) {
        this.entityManager = entityManager;
        this.create = create;
    }

    public Response getLogs(LogType logType, int page){

        Condition condition = DSL.trueCondition();
        if(logType != LogType.ALL){
            condition = condition.and(Log.LOG.LOG_TYPE.eq(logType.toString()));
        }

        Select<?> countQuery =
            create.select(DSL.count())
                .from(Log.LOG)
                .where(condition);

        Query q1 = entityManager.createNativeQuery(countQuery.getSQL());
        API.setBindParameterValues(q1, countQuery);
        Object countResult = q1.getSingleResult();


        Select<?> query =
            create.select()
                .from(Log.LOG)
                .where(condition)
                .orderBy(Log.LOG.CREATED_AT.desc())
                .limit(20)
                .offset((page-1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List queryResult = q.getResultList();

        List result = new ArrayList<>();
        result.add(Integer.parseInt(String.valueOf(countResult)));
        result.add(queryResult);


        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }
}
