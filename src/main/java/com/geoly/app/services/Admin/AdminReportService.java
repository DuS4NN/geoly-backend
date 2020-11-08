package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Quest;
import com.geoly.app.jooq.tables.QuestReport;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.jooq.tables.UserReport;
import com.geoly.app.models.Log;
import com.geoly.app.models.LogType;
import com.geoly.app.models.StatusMessage;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Update;
import org.jooq.impl.DSL;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.count;

@Service
public class AdminReportService {

    private EntityManager entityManager;
    private DSLContext create;

    public AdminReportService(EntityManager entityManager, DSLContext create){
        this.entityManager = entityManager;
        this.create = create;
    }

    public Response getUserReports(String nick, int page){

        Condition condition = DSL.trueCondition();

        if(!nick.equals("")){
            condition = condition.and(User.USER.NICK_NAME.like("%"+nick+"%"));
        }

        Select<?> count =
            create.select(count())
                .from(create.select(count())
                        .from(UserReport.USER_REPORT)
                        .leftJoin(User.USER)
                        .on(User.USER.ID.eq(UserReport.USER_REPORT.REPORTED))
                        .where(condition)
                        .and(UserReport.USER_REPORT.SOLVED.isFalse())
                        .groupBy(UserReport.USER_REPORT.REPORTED)
                        .asTable("reported"));

        Query q1 = entityManager.createNativeQuery(count.getSQL());
        API.setBindParameterValues(q1, count);
        Object countResult = q1.getSingleResult();

        Select<?> query =
            create.select(count().as("count"), UserReport.USER_REPORT.REPORTED, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL)
                .from(UserReport.USER_REPORT)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(UserReport.USER_REPORT.REPORTED))
                .where(condition)
                .and(UserReport.USER_REPORT.SOLVED.isFalse())
                .groupBy(UserReport.USER_REPORT.REPORTED)
                .orderBy(count().desc())
                .limit(20)
                .offset((page - 1)* 20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List queryResult = q.getResultList();

        List result = new ArrayList();
        result.add(countResult);
        result.add(queryResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getUserReportDetails(int id){

        Select<?> solved =
            create.select(count(), UserReport.USER_REPORT.REASON)
                .from(UserReport.USER_REPORT)
                .where(UserReport.USER_REPORT.SOLVED.isTrue())
                .and(UserReport.USER_REPORT.REPORTED.eq(id))
                .groupBy(UserReport.USER_REPORT.REASON);

        Query q1 = entityManager.createNativeQuery(solved.getSQL());
        API.setBindParameterValues(q1, solved);
        List solvedResult = q1.getResultList();

        Select<?> unsolved =
                create.select(count(), UserReport.USER_REPORT.REASON)
                        .from(UserReport.USER_REPORT)
                        .where(UserReport.USER_REPORT.SOLVED.isFalse())
                        .and(UserReport.USER_REPORT.REPORTED.eq(id))
                        .groupBy(UserReport.USER_REPORT.REASON);

        Query q2 = entityManager.createNativeQuery(unsolved.getSQL());
        API.setBindParameterValues(q2, unsolved);
        List unsolvedResult = q2.getResultList();

        List<List> result = new ArrayList<>();
        result.add(solvedResult);
        result.add(unsolvedResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response solveUserReport(int id, int adminId){
       Update<?> query = create.update(UserReport.USER_REPORT)
            .set(UserReport.USER_REPORT.SOLVED, DSL.coerce(1, Byte.class))
            .where(UserReport.USER_REPORT.REPORTED.eq(id))
            .and(UserReport.USER_REPORT.SOLVED.isFalse());

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        q.executeUpdate();

        Log log = new Log();
        log.setLogType(LogType.SOLVE_REPORT);

        JSONObject jo = new JSONObject();
        jo.put("adminId", adminId);
        jo.put("userId", id);
        log.setData(jo.toString());

        entityManager.persist(log);

        return new Response(StatusMessage.REPORTS_SOLVED, HttpStatus.ACCEPTED, null);
    }


    public Response getQuestReports(String name, int page){

        Condition condition = DSL.trueCondition();

        if(!name.equals("")){
            condition = condition.and(Quest.QUEST.NAME.like("%"+name+"%"));
        }

        Select<?> count =
                create.select(count())
                        .from(create.select(count())
                                .from(QuestReport.QUEST_REPORT)
                                .leftJoin(Quest.QUEST)
                                .on(Quest.QUEST.ID.eq(QuestReport.QUEST_REPORT.QUEST_ID))
                                .where(condition)
                                .and(QuestReport.QUEST_REPORT.SOLVED.isFalse())
                                .groupBy(QuestReport.QUEST_REPORT.QUEST_ID)
                                .asTable("reported"));

        Query q1 = entityManager.createNativeQuery(count.getSQL());
        API.setBindParameterValues(q1, count);
        Object countResult = q1.getSingleResult();

        Select<?> query =
                create.select(count().as("count"), QuestReport.QUEST_REPORT.QUEST_ID, Quest.QUEST.NAME)
                        .from(QuestReport.QUEST_REPORT)
                        .leftJoin(Quest.QUEST)
                        .on(Quest.QUEST.ID.eq(QuestReport.QUEST_REPORT.QUEST_ID))
                        .where(condition)
                        .and(QuestReport.QUEST_REPORT.SOLVED.isFalse())
                        .groupBy(QuestReport.QUEST_REPORT.ID)
                        .orderBy(count().desc())
                        .limit(20)
                        .offset((page - 1)* 20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List queryResult = q.getResultList();

        List result = new ArrayList();
        result.add(countResult);
        result.add(queryResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getQuestReportDetails(int id){

        Select<?> solved =
                create.select(count(), QuestReport.QUEST_REPORT.REASON)
                        .from(QuestReport.QUEST_REPORT)
                        .where(QuestReport.QUEST_REPORT.SOLVED.isTrue())
                        .and(QuestReport.QUEST_REPORT.QUEST_ID.eq(id))
                        .groupBy(QuestReport.QUEST_REPORT.REASON);

        Query q1 = entityManager.createNativeQuery(solved.getSQL());
        API.setBindParameterValues(q1, solved);
        List solvedResult = q1.getResultList();

        Select<?> unsolved =
                create.select(count(), QuestReport.QUEST_REPORT.REASON)
                        .from(QuestReport.QUEST_REPORT)
                        .where(QuestReport.QUEST_REPORT.SOLVED.isFalse())
                        .and(QuestReport.QUEST_REPORT.QUEST_ID.eq(id))
                        .groupBy(QuestReport.QUEST_REPORT.REASON);

        Query q2 = entityManager.createNativeQuery(unsolved.getSQL());
        API.setBindParameterValues(q2, unsolved);
        List unsolvedResult = q2.getResultList();

        List<List> result = new ArrayList<>();
        result.add(solvedResult);
        result.add(unsolvedResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response solveQuestReport(int id, int adminId){
        Update<?> query = create.update(QuestReport.QUEST_REPORT)
                .set(QuestReport.QUEST_REPORT.SOLVED, DSL.coerce(1, Byte.class))
                .where(QuestReport.QUEST_REPORT.QUEST_ID.eq(id))
                .and(QuestReport.QUEST_REPORT.SOLVED.isFalse());

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        q.executeUpdate();

        Log log = new Log();
        log.setLogType(LogType.SOLVE_REPORT);

        JSONObject jo = new JSONObject();
        jo.put("adminId", adminId);
        jo.put("questId", id);
        log.setData(jo.toString());

        entityManager.persist(log);

        return new Response(StatusMessage.REPORTS_SOLVED, HttpStatus.ACCEPTED, null);
    }
}
