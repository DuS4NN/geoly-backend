package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminStatsService {

    private EntityManager entityManager;
    private DSLContext create;


    public AdminStatsService(EntityManager entityManager, DSLContext create) {
        this.entityManager = entityManager;
        this.create = create;
    }

    public Response getStats(){

        LocalDateTime date = LocalDateTime.now().minusDays(7);
        Timestamp timestamp = Timestamp.valueOf(date);

        Table<?> newUsers =
            create.select(DSL.count().as("newUsers"))
                .from(User.USER)
                .where(User.USER.CREATED_AT.greaterThan(timestamp))
                .asTable("newUsersTable");

        Table<?> finishedQuests = getTable(0, timestamp);
        Table<?> finishedDaily = getTable(1, timestamp);

        Select<?> query =
            create.select(newUsers.field("newUsers"), finishedQuests.field("count-0"), finishedDaily.field("count-1"))
                .from(newUsers, finishedQuests, finishedDaily);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List info = q.getResultList();

        List categoryRatio = categoryRatio();
        List newQuests = newQuests();

        List<List> result = new ArrayList<>();
        result.add(info);
        result.add(categoryRatio);
        result.add(newQuests);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }


    private Table getTable(int daily, Timestamp timestamp){
        return
            create.select(DSL.count().as("count-"+daily))
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
                .where(Stage.STAGE.ID.in(
                    create.select(DSL.max(Stage.STAGE.ID))
                        .from(Stage.STAGE)
                        .groupBy(Stage.STAGE.QUEST_ID)
                ))
                    .and(UserQuest.USER_QUEST.UPDATED_AT.greaterThan(timestamp))
                    .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.toString()))
                    .and(Quest.QUEST.DAILY.eq(DSL.coerce(daily, Byte.class)))
                .asTable("tableDaily-"+daily);
    }

    private List categoryRatio(){

        Select<?> query =
            create.select(Quest.QUEST.CATEGORY_ID, Category.CATEGORY.NAME, DSL.count())
                .from(Quest.QUEST)
                .leftJoin(Category.CATEGORY)
                    .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                .groupBy(Quest.QUEST.CATEGORY_ID);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);

        return q.getResultList();
    }

    private List newQuests(){

        LocalDateTime date = LocalDateTime.now().minusDays(30);
        Timestamp timestamp = Timestamp.valueOf(date);

        Select<?> query =
            create.select(DSL.count(), DSL.day(Quest.QUEST.CREATED_AT), DSL.month(Quest.QUEST.CREATED_AT))
                .from(Quest.QUEST)
                .where(Quest.QUEST.CREATED_AT.greaterThan(timestamp))
                .groupBy(DSL.concat(DSL.day(Quest.QUEST.CREATED_AT), DSL.month(Quest.QUEST.CREATED_AT), DSL.year(Quest.QUEST.CREATED_AT)))
                .orderBy(Quest.QUEST.CREATED_AT);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);

        return q.getResultList();
    }
}