package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.PartyQuest;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.jooq.tables.UserPartyQuest;
import com.geoly.app.jooq.tables.UserQuest;
import com.geoly.app.models.*;
import com.geoly.app.repositories.*;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserQuestRepository userQuestRepository;
    private StageRepository stageRepository;
    private UserRepository userRepository;
    private UserPartyQuestRepository userPartyQuestRepository;
    private QuestRepository questRepository;
    private BadgeRepository badgeRepository;
    private UserBadgeRepository userBadgeRepository;
    private NotificationService notificationService;

    public GameService(EntityManager entityManager, DSLContext create, UserQuestRepository userQuestRepository, StageRepository stageRepository, UserRepository userRepository, UserPartyQuestRepository userPartyQuestRepository, QuestRepository questRepository, BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository, NotificationService notificationService) {
        this.entityManager = entityManager;
        this.create = create;
        this.userQuestRepository = userQuestRepository;
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
        this.userPartyQuestRepository = userPartyQuestRepository;
        this.questRepository = questRepository;
        this.badgeRepository = badgeRepository;
        this.userBadgeRepository = userBadgeRepository;
        this.notificationService = notificationService;
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
            create.select(Stage.STAGE.ID, Stage.STAGE.ANSWER, Stage.STAGE.LATITUDE, Stage.STAGE.LONGITUDE, Stage.STAGE.QR_CODE_URL, Stage.STAGE.QUESTION, Stage.STAGE.TYPE, Stage.STAGE.ADVISE, Stage.STAGE.NOTE, Stage.STAGE.ANSWERS_LIST, Stage.STAGE.QUEST_ID)
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
            create.select(Stage.STAGE.ID, Stage.STAGE.ANSWER, Stage.STAGE.LATITUDE, Stage.STAGE.LONGITUDE, Stage.STAGE.QR_CODE_URL, Stage.STAGE.QUESTION, Stage.STAGE.TYPE, Stage.STAGE.ADVISE, Stage.STAGE.NOTE, Stage.STAGE.ANSWERS_LIST, Stage.STAGE.QUEST_ID)
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

    @Transactional(rollbackOn = Exception.class)
    public Response finishStageAndStartNewInClassic(int stageId, int questId, int userId){
        Response response = getUserQuest(stageId, userId);
        if(response.getResponseEntity().getStatusCode() == HttpStatus.NOT_FOUND) return response;

        List data = response.getData();
        com.geoly.app.models.UserQuest userQuest = (com.geoly.app.models.UserQuest) data.get(0);
        User user = (User) data.get(1);

        userQuest.setStatus(UserQuestStatus.FINISHED);
        entityManager.merge(userQuest);

        Optional<com.geoly.app.models.Stage> stage = stageRepository.findById(getIdOfNewStage(questId, stageId));
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        com.geoly.app.models.UserQuest newUserQuest = new com.geoly.app.models.UserQuest();
        newUserQuest.setStatus(UserQuestStatus.ON_STAGE);
        newUserQuest.setWrongAnswers(0);
        newUserQuest.setAdviseUsed(false);
        newUserQuest.setStage(stage.get());
        newUserQuest.setUser(user);
        entityManager.persist(newUserQuest);

        return new Response(StatusMessage.OK, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response finishStageAndStartNewInParty(int stageId, int questId, int userId){
        Optional<com.geoly.app.models.Stage> stage = stageRepository.findById(stageId);
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.UserPartyQuest> userPartyQuest = userPartyQuestRepository.findByUserAndStageAndStatus(user.get(), stage.get(), UserQuestStatus.ON_STAGE);
        if(!userPartyQuest.isPresent()) return new Response(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        userPartyQuest.get().setStatus(UserQuestStatus.FINISHED);
        entityManager.merge(userPartyQuest.get());

        Optional<com.geoly.app.models.Stage> newStage = stageRepository.findById(getIdOfNewStage(questId, stageId));
        if(!newStage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        com.geoly.app.models.UserPartyQuest newUserPartyQuest = new com.geoly.app.models.UserPartyQuest();
        newUserPartyQuest.setStatus(UserQuestStatus.ON_STAGE);
        newUserPartyQuest.setStage(newStage.get());
        newUserPartyQuest.setUser(user.get());
        entityManager.persist(newUserPartyQuest);

        return new Response(StatusMessage.OK, HttpStatus.ACCEPTED, null);
    }

    private int getIdOfNewStage(int questId, int stageId){
        Select<?> newStageIdQuery =
                create.select(Stage.STAGE.ID)
                        .from(Stage.STAGE)
                        .where(Stage.STAGE.QUEST_ID.eq(questId))
                        .and(Stage.STAGE.ID.greaterThan(stageId))
                        .orderBy(Stage.STAGE.ID)
                        .limit(1);

        Query q = entityManager.createNativeQuery(newStageIdQuery.getSQL());
        API.setBindParameterValues(q, newStageIdQuery);
        Object newStageIdObject = q.getSingleResult();

        return Integer.parseInt(String.valueOf(newStageIdObject));
    }

    private Response getUserQuest(int stageId, int userId){
        Optional<com.geoly.app.models.Stage> stage = stageRepository.findById(stageId);
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);


        Optional<com.geoly.app.models.UserQuest> userQuest = userQuestRepository.findByUserAndStageAndStatus(user.get(), stage.get(), UserQuestStatus.ON_STAGE);
        if(!userQuest.isPresent()) return new Response(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        List<Object> result = new ArrayList<>();
        result.add(userQuest.get());
        result.add(user.get());

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response finishQuestInClassic(int stageId, int userId, int questId, String type){
        Optional<com.geoly.app.models.Stage> stage = stageRepository.findById(stageId);
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.UserQuest> userQuest = userQuestRepository.findByUserAndStageAndStatus(user.get(), stage.get(), UserQuestStatus.ON_STAGE);
        if(!userQuest.isPresent()) return new Response(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        userQuest.get().setStatus(UserQuestStatus.FINISHED);
        entityManager.merge(userQuest.get());

        givePoints(questId, userId, type, stage.get());
        giveFinishBadge(user.get(), questId);

        return new Response(StatusMessage.OK, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response finishQuestInParty(int stageId, int userId){
        Optional<com.geoly.app.models.Stage> stage = stageRepository.findById(stageId);
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.UserPartyQuest> userPartyQuest = userPartyQuestRepository.findByUserAndStageAndStatus(user.get(), stage.get(), UserQuestStatus.ON_STAGE);
        if(!userPartyQuest.isPresent()) return new Response(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        userPartyQuest.get().setStatus(UserQuestStatus.FINISHED);
        entityManager.merge(userPartyQuest.get());

        return new Response(StatusMessage.OK, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    @Async
    public void givePoints(int questId, int userId, String type, com.geoly.app.models.Stage stage){
        int finalPoints;

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return;

        if(type.equals("DAILY")){
            finalPoints = 30;
            giveStageBadge(stage, user.get());
        }else{
            finalPoints = 100;

            int answerQuestionPoints = 20;
            int scanQrCodePoints = 30;
            int goToPlacePoints = 40;

            int useAdvise = 10;
            int wrongAnswer = 2;
            int maxMinusPoints = 20;

            Optional<Quest> quest = questRepository.findById(questId);
            if(!quest.isPresent()) return;

            int countOfStages = quest.get().getStage().size();

            for(com.geoly.app.models.Stage s : quest.get().getStage()){
                switch (s.getType()){
                    case ANSWER_QUESTION:
                        finalPoints += answerQuestionPoints;
                        break;
                    case SCAN_QR_CODE:
                        finalPoints += scanQrCodePoints;
                        break;
                    case GO_TO_PLACE:
                        finalPoints += goToPlacePoints;
                        break;
                }

                giveStageBadge(s, user.get());
            }

            Select<?> query =
                    create.select(DSL.sum(UserQuest.USER_QUEST.WRONG_ANSWERS), DSL.sum(UserQuest.USER_QUEST.ADVISE_USED))
                            .from(UserQuest.USER_QUEST)
                            .leftJoin(Stage.STAGE)
                            .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                            .where(UserQuest.USER_QUEST.USER_ID.eq(userId))
                            .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                            .and(Stage.STAGE.QUEST_ID.eq(questId))
                            .orderBy(UserQuest.USER_QUEST.ID.desc())
                            .limit(countOfStages);

            Query q = entityManager.createNativeQuery(query.getSQL());
            API.setBindParameterValues(q, query);
            Object[] result = (Object[]) q.getSingleResult();

            int minusPoints = Integer.parseInt(String.valueOf(result[1]))*useAdvise + Integer.parseInt(String.valueOf(result[0]))*wrongAnswer;
            if(minusPoints > maxMinusPoints) finalPoints -= maxMinusPoints;
            else finalPoints -= minusPoints;
        }

       Point point = new Point();
       point.setUser(user.get());
       point.setAmount(finalPoints);
       entityManager.persist(point);

        HashMap<String, Object> data = new HashMap<>();
        data.put("pointAmount", finalPoints);
        data.put("questId", questId);
        notificationService.sendNotification(user.get(), NotificationType.GET_POINTS, data,true);
    }

    @Transactional(rollbackOn = Exception.class)
    @Async
    public void giveStageBadge(com.geoly.app.models.Stage stage, User user){
        int[] questionTiers = {200, 100, 50, 10};
        int[] qrCodeTiers = {100, 50, 10, 5};
        int[] placeTiers = {300, 150, 50, 20};

        int[] stageTiers = {0,0,0,0};

        switch (stage.getType()){
            case GO_TO_PLACE:
                stageTiers = placeTiers;
                break;
            case SCAN_QR_CODE:
                stageTiers = qrCodeTiers;
                break;
            case ANSWER_QUESTION:
                stageTiers = questionTiers;
                break;
        }

        Select<?> query =
            create.select(DSL.count())
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                .where(UserQuest.USER_QUEST.USER_ID.eq(user.getId()))
                .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                .and(Stage.STAGE.TYPE.eq(stage.getType().name()));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        int count = Integer.parseInt(String.valueOf(q.getSingleResult()));

        Badge badge;

        UserBadge userBadge = new UserBadge();
        userBadge.setUser(user);


        if(count >= stageTiers[0]){
            switch (stage.getType()){
                case GO_TO_PLACE:
                    badge = badgeRepository.getBadgeByName(BadgeType.PLACE_300.name());
                    break;
                case SCAN_QR_CODE:
                    badge = badgeRepository.getBadgeByName(BadgeType.QRCODE_100.name());
                    break;
                case ANSWER_QUESTION:
                    badge = badgeRepository.getBadgeByName(BadgeType.QUESTION_200.name());
                    break;
                default:
                    return;
            }
        }else if(count >= stageTiers[1]){
            switch (stage.getType()){
                case GO_TO_PLACE:
                    badge = badgeRepository.getBadgeByName(BadgeType.PLACE_150.name());
                    break;
                case SCAN_QR_CODE:
                    badge = badgeRepository.getBadgeByName(BadgeType.QRCODE_50.name());
                    break;
                case ANSWER_QUESTION:
                    badge = badgeRepository.getBadgeByName(BadgeType.QUESTION_100.name());
                    break;
                default:
                    return;
            }
        }else if(count >= stageTiers[2]){
            switch (stage.getType()){
                case GO_TO_PLACE:
                    badge = badgeRepository.getBadgeByName(BadgeType.PLACE_50.name());
                    break;
                case SCAN_QR_CODE:
                    badge = badgeRepository.getBadgeByName(BadgeType.QRCODE_10.name());
                    break;
                case ANSWER_QUESTION:
                    badge = badgeRepository.getBadgeByName(BadgeType.QUESTION_50.name());
                    break;
                default:
                    return;
            }
        }else if(count >= stageTiers[3]){
            switch (stage.getType()){
                case GO_TO_PLACE:
                    badge = badgeRepository.getBadgeByName(BadgeType.PLACE_20.name());
                    break;
                case SCAN_QR_CODE:
                    badge = badgeRepository.getBadgeByName(BadgeType.QRCODE_5.name());
                    break;
                case ANSWER_QUESTION:
                    badge = badgeRepository.getBadgeByName(BadgeType.QUESTION_10.name());
                    break;
                default:
                    return;
            }
        }else{
            return;
        }

        userBadge.setBadge(badge);

        Optional<UserBadge> userBadgeExist = userBadgeRepository.findByUserAndBadge(user, badge);
        if(!userBadgeExist.isPresent()){
            entityManager.persist(userBadge);

            HashMap<String, Object> data = new HashMap<>();
            data.put("badgeName", badge.getName());
            data.put("questId", stage.getQuest().getId());
            notificationService.sendNotification(user, NotificationType.GET_BADGE, data,true);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Async
    public void giveFinishBadge(User user, int questId){
        int[] finalTiers = {500, 250, 100, 50};

        Table<?> stageIds =
            create.select(DSL.max(Stage.STAGE.ID).as("maxStageId"))
                .from(Stage.STAGE)
                .groupBy(Stage.STAGE.QUEST_ID)
                .asTable("stageIds");

        Select<?> query =
            create.select(DSL.count())
                .from(UserQuest.USER_QUEST, stageIds)
                .where(UserQuest.USER_QUEST.USER_ID.eq(user.getId()))
                .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                .and(UserQuest.USER_QUEST.STAGE_ID.in(stageIds.field("maxStageId")));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        int count = Integer.parseInt(String.valueOf(q.getSingleResult()));

        Badge badge;

        UserBadge userBadge = new UserBadge();
        userBadge.setUser(user);

        if(count >= finalTiers[0]){
            badge = badgeRepository.getBadgeByName(BadgeType.FINISH_500.name());
        }else if(count >= finalTiers[1]){
            badge = badgeRepository.getBadgeByName(BadgeType.FINISH_250.name());
        }else if(count >= finalTiers[2]){
            badge = badgeRepository.getBadgeByName(BadgeType.FINISH_100.name());
        }else if(count >= finalTiers[3]){
            badge = badgeRepository.getBadgeByName(BadgeType.FINISH_50.name());
        }else{
            return;
        }

        userBadge.setBadge(badge);

        Optional<UserBadge> userBadgeExist = userBadgeRepository.findByUserAndBadge(user, badge);
        if(!userBadgeExist.isPresent()){
            entityManager.persist(userBadge);

            HashMap<String, Object> data = new HashMap<>();
            data.put("badgeName", badge.getName());
            data.put("questId", questId);
            notificationService.sendNotification(user, NotificationType.GET_BADGE, data,true);
        }
    }
}