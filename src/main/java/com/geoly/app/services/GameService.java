package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.PartyQuest;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.jooq.tables.UserPartyQuest;
import com.geoly.app.jooq.tables.UserQuest;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class GameService {

    private EntityManager entityManager;
    private DSLContext create;

    public GameService(EntityManager entityManager, DSLContext create) {
        this.entityManager = entityManager;
        this.create = create;
    }


    public Response getUnfinishedStagesClassic(int questId, int userId){

        Select<?> stageId =
            create.select(Stage.STAGE.ID)
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                .where(UserQuest.USER_QUEST.USER_ID.eq(userId))
                .and(Stage.STAGE.QUEST_ID.eq(questId))
                .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
                .orderBy(UserQuest.USER_QUEST.ID);

        Query qStage = entityManager.createNativeQuery(stageId.getSQL());
        API.setBindParameterValues(qStage, stageId);
        int stage = Integer.parseInt(String.valueOf(qStage.getSingleResult()));


        Select<?> query =
            create.select(Stage.STAGE.ID, Stage.STAGE.ANSWER, Stage.STAGE.LATITUDE, Stage.STAGE.LONGITUDE, Stage.STAGE.QR_CODE_URL, Stage.STAGE.QUESTION, Stage.STAGE.TYPE, Stage.STAGE.ADVISE, Stage.STAGE.NOTE, Stage.STAGE.ANSWERS_LIST)
                .from(Stage.STAGE)
                .where(Stage.STAGE.QUEST_ID.eq(questId))
                .and(Stage.STAGE.ID.greaterOrEqual(stage));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getUnfinishedStagesParty(int questId, int userId, int partyId){
        Select<?> stageId =
            create.select(UserPartyQuest.USER_PARTY_QUEST.STAGE_ID)
                .from(UserPartyQuest.USER_PARTY_QUEST)
                .leftJoin(PartyQuest.PARTY_QUEST)
                    .on(PartyQuest.PARTY_QUEST.ID.eq(UserPartyQuest.USER_PARTY_QUEST.PARTY_QUEST_ID))
                .where(PartyQuest.PARTY_QUEST.PARTY_ID.eq(partyId))
                .and(PartyQuest.PARTY_QUEST.QUEST_ID.eq(questId))
                .and(UserPartyQuest.USER_PARTY_QUEST.USER_ID.eq(userId))
                .and(UserPartyQuest.USER_PARTY_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
                .orderBy(UserPartyQuest.USER_PARTY_QUEST.ID);

        Query qStage = entityManager.createNativeQuery(stageId.getSQL());
        API.setBindParameterValues(qStage, stageId);
        int stage = Integer.parseInt(String.valueOf(qStage.getSingleResult()));

        Select<?> query =
            create.select(Stage.STAGE.ID, Stage.STAGE.ANSWER, Stage.STAGE.LATITUDE, Stage.STAGE.LONGITUDE, Stage.STAGE.QR_CODE_URL, Stage.STAGE.QUESTION, Stage.STAGE.TYPE, Stage.STAGE.ADVISE, Stage.STAGE.NOTE, Stage.STAGE.ANSWERS_LIST)
                .from(Stage.STAGE)
                .where(Stage.STAGE.QUEST_ID.eq(questId))
                .and(Stage.STAGE.ID.greaterOrEqual(stage));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }
}