package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.NotificationType;
import com.geoly.app.models.QuestReportReason;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.*;
import com.sun.org.apache.regexp.internal.RE;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

import static org.jooq.impl.DSL.*;

@Service
public class QuestDetailService {

    private EntityManager entityManager;
    private DSLContext create;
    private NotificationService notificationService;
    private QuestRepository questRepository;
    private UserRepository userRepository;
    private QuestReviewRepository questReviewRepository;
    private StageRepository stageRepository;
    private QuestReportRepository questReportRepository;
    private UserQuestRepository userQuestRepository;

    private int REVIEW_ON_PAGE = 5;

    public QuestDetailService(EntityManager entityManager, DSLContext create, NotificationService notificationService, QuestRepository questRepository, UserRepository userRepository, QuestReviewRepository questReviewRepository, StageRepository stageRepository, QuestReportRepository questReportRepository, UserQuestRepository userQuestRepository){
        this.entityManager = entityManager;
        this.create = create;
        this.notificationService = notificationService;
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.questReviewRepository = questReviewRepository;
        this.stageRepository = stageRepository;
        this.questReportRepository = questReportRepository;
        this.userQuestRepository = userQuestRepository;
    }

    private StatusMessage checkIfUserCanStartQuest(int questId, int userId, int questOwnerId){

        if(userId == questOwnerId){
            return StatusMessage.USER_CAN_NOT_PLAY_OWN_QUEST;
        }

        Select<?> activeQuest =
                create.select(count())
                    .from(UserQuest.USER_QUEST)
                    .where(UserQuest.USER_QUEST.USER_ID.eq(userId))
                    .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()));

        Query q = entityManager.createNativeQuery(activeQuest.getSQL());
        GeolyAPI.setBindParameterValues(q, activeQuest);
        Object result = q.getSingleResult();

        if(Integer.parseInt(String.valueOf(result))>0){
            return StatusMessage.USER_HAS_ACTIVE_QUEST;
        }

        Table<?> stages =
                create.select(Stage.STAGE.ID.as("stage_id"))
                        .from(Stage.STAGE)
                        .where(Stage.STAGE.QUEST_ID.eq(questId))
                        .asTable("stage");

        Select<?> alreadyStarted =
                create.select(when(UserQuest.USER_QUEST.STATUS.isNull(), "0").otherwise(UserQuest.USER_QUEST.STATUS))
                    .from(UserQuest.USER_QUEST, stages)
                    .where(UserQuest.USER_QUEST.USER_ID.eq(userId))
                    .and(UserQuest.USER_QUEST.STAGE_ID.in(stages.field("stage_id")))
                    .orderBy(UserQuest.USER_QUEST.ID.desc())
                    .limit(1);

        Query q2 = entityManager.createNativeQuery(alreadyStarted.getSQL());
        GeolyAPI.setBindParameterValues(q2, alreadyStarted);
        List result2 = q2.getResultList();

        if(result2 != null && !result2.isEmpty() && String.valueOf(result2.get(0)).equals(UserQuestStatus.FINISHED.name() )){
            System.out.println(String.valueOf(result2));
            return StatusMessage.USER_ALREADY_FINISHED_QUEST;
        }

        return StatusMessage.OK;
    }

    private StatusMessage checkIfUserCanAddReview(int questId, int userId){
        Table<?> ifReviewExist =
                create.select(count().as("count_exist"))
                        .from(QuestReview.QUEST_REVIEW)
                        .where(QuestReview.QUEST_REVIEW.QUEST_ID.eq(questId))
                        .and(QuestReview.QUEST_REVIEW.USER_ID.eq(userId))
                        .asTable("exist_review");

        Table<?> ifUserStartedQuest =
                create.select(count().as("count_quest"))
                        .from(UserQuest.USER_QUEST)
                        .where(UserQuest.USER_QUEST.STAGE_ID.in(
                                create.select(Stage.STAGE.ID)
                                        .from(Stage.STAGE)
                                        .where(Stage.STAGE.QUEST_ID.eq(questId))))
                        .and(UserQuest.USER_QUEST.USER_ID.eq(userId))
                        .asTable("user_quest");

        Select<?> query =
                create.select(ifReviewExist.field("count_exist"), ifUserStartedQuest.field("count_quest"))
                        .from(ifReviewExist, ifUserStartedQuest);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        Object[] result = (Object[]) q.getSingleResult();

        if(Integer.parseInt(String.valueOf(result[0])) > 0) return StatusMessage.REVIEW_ALREADY_EXIST;
        if(Integer.parseInt(String.valueOf(result[1])) < 0) return StatusMessage.USER_DOESNT_PLAY_QUEST;

        return StatusMessage.OK;
    }

    public Response getReviewCountAndWritable(int questId, int userId){
        Select<?> query =
                create.select(count())
                .from(QuestReview.QUEST_REVIEW)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(QuestReview.QUEST_REVIEW.USER_ID))
                .where(User.USER.ACTIVE.isTrue())
                .and(QuestReview.QUEST_REVIEW.QUEST_ID.eq(questId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        Object result = q.getSingleResult();

        StatusMessage response = checkIfUserCanAddReview(questId, userId);

        ArrayList<Integer> data = new ArrayList<>();
        data.add(Integer.parseInt(String.valueOf(result)));

        if(response == StatusMessage.OK){
            data.add(1);
        }else{
            data.add(0);
        }

        return new Response(StatusMessage.OK, HttpStatus.OK, data);

    }

    public Response getReviewsOfQuest(int id, int userId, int page){
        Select<?> query =
            create.select(when(QuestReview.QUEST_REVIEW.USER_ID.eq(userId), 1).otherwise(0),
                        QuestReview.QUEST_REVIEW.ID, QuestReview.QUEST_REVIEW.REVIEW_TEXT, QuestReview.QUEST_REVIEW.REVIEW,
                    when(QuestReview.QUEST_REVIEW.CREATED_AT.isNull(), QuestReview.QUEST_REVIEW.UPDATE_AT).otherwise(QuestReview.QUEST_REVIEW.CREATED_AT),
                    User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL)
            .from(QuestReview.QUEST_REVIEW)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(QuestReview.QUEST_REVIEW.USER_ID))
            .where(User.USER.ACTIVE.isTrue())
            .and(QuestReview.QUEST_REVIEW.QUEST_ID.eq(id))
            .orderBy(when(QuestReview.QUEST_REVIEW.CREATED_AT.isNull(), QuestReview.QUEST_REVIEW.UPDATE_AT).otherwise(QuestReview.QUEST_REVIEW.CREATED_AT).desc())
            .limit(REVIEW_ON_PAGE)
            .offset((page-1)*REVIEW_ON_PAGE);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.NO_REVIEW, HttpStatus.NO_CONTENT, null);
        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getStagesOfQuest(int id){
        Select<?> query =
            create.select()
            .from(Stage.STAGE)
            .where(Stage.STAGE.QUEST_ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getDetailsOfQuest(int id, int userId){
        Table<?> avgReview =
                create.select(avg(QuestReview.QUEST_REVIEW.REVIEW).as("avg"), QuestReview.QUEST_REVIEW.ID.as("id"))
                        .from(QuestReview.QUEST_REVIEW)
                        .where(QuestReview.QUEST_REVIEW.QUEST_ID.eq(id))
                        .groupBy(QuestReview.QUEST_REVIEW.QUEST_ID)
                        .asTable("review");

        Table<?> countFinished =
                create.select(count(Stage.STAGE.ID).as("finished"))
                        .from(UserQuest.USER_QUEST)
                        .leftJoin(Stage.STAGE)
                        .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                        .where(Stage.STAGE.QUEST_ID.eq(id))
                        .and(UserQuest.USER_QUEST.ID.eq(
                                create.select(max(Stage.STAGE.ID))
                                        .from(Stage.STAGE)
                                        .where(Stage.STAGE.QUEST_ID.eq(id))
                                        .groupBy(Stage.STAGE.QUEST_ID)))
                        .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                        .asTable("count_finished");

        UserQuest u1 = UserQuest.USER_QUEST.as("u1");
        UserQuest u2 = UserQuest.USER_QUEST.as("u2");

        Table<?> countOnStage =
                create.select(count().as("on_stage"))
                        .from(u1)
                        .leftJoin(u2)
                        .on(u1.USER_ID.eq(u2.USER_ID))
                        .and(u1.ID.lessThan(u2.ID))
                        .leftJoin(Stage.STAGE).on(Stage.STAGE.ID.eq(u1.STAGE_ID))
                        .where(u2.ID.isNull())
                        .and(Stage.STAGE.QUEST_ID.eq(id))
                        .and(u1.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
                        .asTable("count_onstage");

        Table<?> countCanceled =
                create.select(count().as("canceled"))
                        .from(u1)
                        .leftJoin(u2)
                        .on(u1.USER_ID.eq(u2.USER_ID))
                        .and(u1.ID.lessThan(u2.ID))
                        .leftJoin(Stage.STAGE).on(Stage.STAGE.ID.eq(u1.STAGE_ID))
                        .where(u2.ID.isNull())
                        .and(Stage.STAGE.QUEST_ID.eq(id))
                        .and(u1.STATUS.eq(UserQuestStatus.CANCELED.name()))
                        .asTable("count_canceled");

        Select<?> query =
            create.select(Quest.QUEST.ID, Quest.QUEST.NAME.as("questName"), Quest.QUEST.DIFFICULTY, Quest.QUEST.DESCRIPTION, Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, avgReview.field("avg"), countFinished.field("finished"), countOnStage.field("on_stage"), countCanceled.field("canceled"), Quest.QUEST.CREATED_AT, Quest.QUEST.PRIVATE_QUEST,
                            when(Quest.QUEST.USER_ID.eq(userId), 1).otherwise(0), Quest.QUEST.PREMIUM)
            .from(countFinished, countOnStage, countCanceled, Quest.QUEST)
            .leftJoin(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
            .leftJoin(avgReview)
                    .on(avgReview.field("id").isNotNull())
            .where(User.USER.ACTIVE.isTrue())
            .and(Quest.QUEST.ACTIVE.isTrue())
            .and(Quest.QUEST.DAILY.isFalse())
            .and(Quest.QUEST.ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getImagesOfQuest(int id){
        Select<?> query =
            create.select(Image.IMAGE.IMAGE_URL)
            .from(Image.IMAGE)
            .where(Image.IMAGE.QUEST_ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.NO_IMAGES, HttpStatus.NO_CONTENT, null);
        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response createReview(int userId, int questId, com.geoly.app.models.QuestReview questReview){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndDaily(questId, false);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        StatusMessage response = checkIfUserCanAddReview(questId, userId);

        if(response != StatusMessage.OK){
            return new Response(response, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        questReview.setQuest(quest.get());
        questReview.setUser(user.get());
        entityManager.persist(questReview);

        ArrayList review = new ArrayList();
        review.add(userId);
        review.add(questReview.getId());
        review.add(user.get().getNickName());
        review.add(user.get().getProfileImageUrl());

        HashMap<String, Object> data = new HashMap<>();
        data.put("userNick", user.get().getNickName());
        data.put("reviewId", questReview.getId());
        data.put("questId", quest.get().getId());
        notificationService.sendNotification(user.get(), NotificationType.ADD_REVIEW, data,true);

        return new Response(StatusMessage.REVIEW_ADDED, HttpStatus.OK, review);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response removeReview(int userId, int reviewId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        Optional<com.geoly.app.models.QuestReview> questReview = questReviewRepository.findByIdAndUser(reviewId, user.get());
        if(!questReview.isPresent()) return new Response(StatusMessage.REVIEW_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        entityManager.remove(questReview.get());
        return new Response(StatusMessage.REVIEW_DELETED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response updateReview(int userId, com.geoly.app.models.QuestReview questReview, int questId){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<com.geoly.app.models.QuestReview> review = questReviewRepository.findByIdAndUser(questReview.getId(), user.get());
        if(!review.isPresent()) return new Response(StatusMessage.REVIEW_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        questReview.setUser(user.get());
        questReview.setQuest(quest.get());

        entityManager.merge(questReview);
        return new Response(StatusMessage.REVIEW_EDITED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response signUpOnQuest(int userId, int questId){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndDailyAndPrivateQuest(questId, false, false);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        if(quest.get().isPremium()){
            Select<?> query =
                create.select(Premium.PREMIUM.ID)
                    .from(Premium.PREMIUM)
                    .where(Premium.PREMIUM.USER_ID.eq(userId))
                    .and(Premium.PREMIUM.END_AT.greaterThan(currentTimestamp()));

            Query q = entityManager.createNativeQuery(query.getSQL());
            GeolyAPI.setBindParameterValues(q, query);
            if(q.getResultList().isEmpty()) return new Response(StatusMessage.USER_DOESNT_HAVE_PREMIUM, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<List<com.geoly.app.models.Stage>> stage = stageRepository.findAllByQuest(quest.get());
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        StatusMessage statusMessage = checkIfUserCanStartQuest(questId, userId, quest.get().getUser().getId());

        if(statusMessage != StatusMessage.OK){
            return new Response(statusMessage, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        com.geoly.app.models.UserQuest userQuest = new com.geoly.app.models.UserQuest();
        userQuest.setStatus(UserQuestStatus.ON_STAGE);
        userQuest.setUser(user.get());
        userQuest.setStage(stage.get().get(0));
        entityManager.persist(userQuest);

        return new Response(StatusMessage.USER_SIGNED_UP_ON_QUEST, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response signOutOfQuest(int userId, int questId){
        Select<?> query =
            create.select(UserQuest.USER_QUEST.ID)
            .from(UserQuest.USER_QUEST)
            .join(Stage.STAGE)
                .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
            .where(Stage.STAGE.QUEST_ID.eq(questId))
            .and(UserQuest.USER_QUEST.USER_ID.eq(userId))
            .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
            .orderBy(UserQuest.USER_QUEST.ID.desc())
            .limit(1);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        Object stageId = q.getSingleResult();

        Optional<com.geoly.app.models.UserQuest> userQuest = userQuestRepository.findById(Integer.parseInt(String.valueOf(stageId)));
        if(!userQuest.isPresent()) return new Response(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        userQuest.get().setStatus(UserQuestStatus.CANCELED);
        entityManager.merge(userQuest.get());

        return new Response(StatusMessage.SIGNED_OUT_OF_QUEST, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response reportQuest(int userId, int questId, QuestReportReason questReportReason){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndDaily(questId, false);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<com.geoly.app.models.QuestReport> report = questReportRepository.findAllByQuestAndUser(quest.get(), user.get());
        if(report.isPresent()) return new Response(StatusMessage.QUEST_REPORT_CREATED, HttpStatus.ACCEPTED, null);

        com.geoly.app.models.QuestReport questReport = new com.geoly.app.models.QuestReport();
        questReport.setReason(questReportReason);
        questReport.setQuest(quest.get());
        questReport.setUser(user.get());
        entityManager.persist(questReport);

        return new Response(StatusMessage.QUEST_REPORT_CREATED, HttpStatus.ACCEPTED, null);
    }
}
