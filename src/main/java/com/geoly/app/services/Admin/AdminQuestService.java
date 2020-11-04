package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Quest;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.jooq.tables.UserQuest;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.repositories.QuestRepository;
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

import static org.jooq.impl.DSL.count;

@Service
public class AdminQuestService {

    private EntityManager entityManager;
    private DSLContext create;
    private QuestRepository questRepository;

    public AdminQuestService(EntityManager entityManager, DSLContext create, QuestRepository questRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.questRepository = questRepository;
    }

    public Response getQuests(String name, int page){

        Condition condition = DSL.trueCondition();
        if(!name.equals("")){
            condition = condition.and(Quest.QUEST.NAME.like("%"+name+"%"));
        }

        Select<?> query =
            create.select(Quest.QUEST.NAME, Quest.QUEST.ID, Quest.QUEST.CREATED_AT)
                .from(Quest.QUEST)
                .where(condition)
                .orderBy(Quest.QUEST.CREATED_AT.desc())
                .limit(20)
                .offset((page - 1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List response = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, response);
    }

    public long getQuestCount(){
        return questRepository.count();
    }

    public Response getQuestDetails(int id){
        Select<?> details =
            create.select(Quest.QUEST.CREATED_AT, Quest.QUEST.NAME, Quest.QUEST.ACTIVE, Quest.QUEST.PREMIUM, Quest.QUEST.PRIVATE_QUEST, Quest.QUEST.DIFFICULTY, Quest.QUEST.DESCRIPTION, Quest.QUEST.CATEGORY_ID, Quest.QUEST.USER_ID)
                .from(Quest.QUEST)
                .where(Quest.QUEST.ID.eq(id));

        Query q1 = entityManager.createNativeQuery(details.getSQL());
        API.setBindParameterValues(q1, details);
        List detailsResult = q1.getResultList();

        Select<?> stages =
            create.select(Stage.STAGE.ANSWERS_LIST, Stage.STAGE.ADVISE, Stage.STAGE.ANSWER, Stage.STAGE.TYPE, Stage.STAGE.QUESTION, Stage.STAGE.QR_CODE_URL, Stage.STAGE.LONGITUDE, Stage.STAGE.LATITUDE, Stage.STAGE.ID, Stage.STAGE.NOTE)
                .from(Stage.STAGE)
                .where(Stage.STAGE.QUEST_ID.eq(id))
                .orderBy(Stage.STAGE.ID.desc());

        Query q2 = entityManager.createNativeQuery(stages.getSQL());
        API.setBindParameterValues(q2, stages);
        List stagesResult = q2.getResultList();

        List<List> result = new ArrayList<>();
        result.add(detailsResult);
        result.add(stagesResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }


    public Response getQuestPlayed(int id, int page, int userId){

        Condition condition = DSL.trueCondition();
        if(userId != 0){
            condition = condition.and(User.USER.ID.eq(userId));
        }


        Select<?> count =
            create.select(count())
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                    .where(Stage.STAGE.QUEST_ID.eq(id))
                    .and(condition);

        Query q1 = entityManager.createNativeQuery(count.getSQL());
        API.setBindParameterValues(q1, count);
        Object countResult = q1.getSingleResult();

        Select<?> query =
            create.select(UserQuest.USER_QUEST.ID.as("userQuestId"), UserQuest.USER_QUEST.UPDATED_AT, UserQuest.USER_QUEST.STATUS, Stage.STAGE.ID.as("stageId"), Stage.STAGE.TYPE, User.USER.NICK_NAME, User.USER.ID.as("userId"), User.USER.PROFILE_IMAGE_URL)
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(UserQuest.USER_QUEST.USER_ID))
                .where(Stage.STAGE.QUEST_ID.eq(id))
                .and(condition)
                .limit(20)
                .offset((page - 1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List resultList = q.getResultList();


        List result = new ArrayList();
        result.add(Integer.parseInt(String.valueOf(countResult)));
        result.add(resultList);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }
}
