package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.PartyQuest;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.jooq.tables.UserPartyQuest;
import com.geoly.app.jooq.tables.UserQuest;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.StageRepository;
import com.geoly.app.repositories.UserQuestRepository;
import com.geoly.app.repositories.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserQuestRepository userQuestRepository;
    private StageRepository stageRepository;
    private UserRepository userRepository;

    public GameService(EntityManager entityManager, DSLContext create, UserQuestRepository userQuestRepository, StageRepository stageRepository, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userQuestRepository = userQuestRepository;
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
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

    @Transactional(rollbackOn = Exception.class)
    public Response getAdvise(int stageId, int userId){
        Response response = getUserQuest(stageId, userId);
        if(response.getResponseEntity().getStatusCode() == HttpStatus.NOT_FOUND) return response;

        com.geoly.app.models.UserQuest userQuest = (com.geoly.app.models.UserQuest) response.getData().get(0);

        userQuest.setAdviseUsed(true);
        entityManager.merge(userQuest);

        return new Response(StatusMessage.OK, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response addWrongAnswer(int stageId, int userId){
        Response response = getUserQuest(stageId, userId);
        if(response.getResponseEntity().getStatusCode() == HttpStatus.NOT_FOUND) return response;

        com.geoly.app.models.UserQuest userQuest = (com.geoly.app.models.UserQuest) response.getData().get(0);

        userQuest.setWrongAnswers(userQuest.getWrongAnswers() + 1);
        entityManager.merge(userQuest);

        return new Response(StatusMessage.OK, HttpStatus.ACCEPTED, null);
    }

    private Response getUserQuest(int stageId, int userId){
        Optional<com.geoly.app.models.Stage> stage = stageRepository.findById(stageId);
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);


        Optional<com.geoly.app.models.UserQuest> userQuest = userQuestRepository.findByUserAndStageAndStatus(user.get(), stage.get(), UserQuestStatus.ON_STAGE);
        if(!userQuest.isPresent()) return new Response(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        List<com.geoly.app.models.UserQuest> result = new ArrayList<>();
        result.add(userQuest.get());

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }
}