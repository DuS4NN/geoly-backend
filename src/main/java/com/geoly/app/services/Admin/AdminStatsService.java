package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Quest;
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
public class AdminStatsService {

    private EntityManager entityManager;
    private DSLContext create;

    public Response getStats(){

        /*
        NEW USERS - Tento tyzden
        FINISHED QUESTS - Tento tyzden
        FINISHED DAILY QUESTS - Tento tyzden

        NEW QUESTS - graf posledny mesiac
        NEW REPORTS - graf posledny mesiac

        NEW PREMIUM USERS - graf posledny mesac
        CATEGORY RATIO - kolac
         */

        return new Response(StatusMessage.OK, HttpStatus.OK, null);
    }

    public List newQuests(){
        Select<?> query =
            create.select(DSL.count(), DSL.day(Quest.QUEST.CREATED_AT), DSL.month(Quest.QUEST.CREATED_AT))
                .from(Quest.QUEST)
                .where(Quest.QUEST.CREATED_AT.between(DSL.currentTimestamp()).and(DSL.currentTimestamp()))
                .groupBy(DSL.concat(DSL.day(Quest.QUEST.CREATED_AT).toString(),DSL.month(Quest.QUEST.CREATED_AT).toString(),DSL.year(Quest.QUEST.CREATED_AT).toString()))
                .orderBy(Quest.QUEST.CREATED_AT);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);

        return q.getResultList();
    }
}
