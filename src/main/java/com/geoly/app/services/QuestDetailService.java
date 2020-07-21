package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.QuestReportReason;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.*;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;

@Service
public class QuestDetailService {

    private EntityManager entityManager;
    private DSLContext create;
    private QuestRepository questRepository;
    private UserRepository userRepository;
    private QuestReviewRepository questReviewRepository;
    private StageRepository stageRepository;
    private QuestReportRepository questReportRepository;

    public QuestDetailService(EntityManager entityManager, DSLContext create, QuestRepository questRepository, UserRepository userRepository, QuestReviewRepository questReviewRepository, StageRepository stageRepository, QuestReportRepository questReportRepository){
        this.entityManager = entityManager;
        this.create = create;
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.questReviewRepository = questReviewRepository;
        this.stageRepository = stageRepository;
        this.questReportRepository = questReportRepository;
    }

    public List getReviewsOfQuest(int id){
        Select<?> query =
            create.select(QuestReview.QUEST_REVIEW.ID, QuestReview.QUEST_REVIEW.REVIEW_TEXT, QuestReview.QUEST_REVIEW.REVIEW, QuestReview.QUEST_REVIEW.CREATED_AT, User.USER.NICK_NAME)
            .from(QuestReview.QUEST_REVIEW)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(QuestReview.QUEST_REVIEW.USER_ID))
            .where(User.USER.ACTIVE.isTrue())
            .and(QuestReview.QUEST_REVIEW.QUEST_ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }

    public List getStagesOfQuest(int id){
        Select<?> query =
            create.select()
            .from(Stage.STAGE)
            .where(Stage.STAGE.QUEST_ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }

    public List getDetailsOfQuest(int id){
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
            create.select(Quest.QUEST.ID, Quest.QUEST.DIFFICULTY, Quest.QUEST.DESCRIPTION, Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME, User.USER.NICK_NAME, avgReview.field("avg"), countFinished.field("finished"), countOnStage.field("on_stage"), countCanceled.field("canceled"))
            .from(countFinished, countOnStage, countCanceled, Quest.QUEST)
            .leftJoin(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
            .leftJoin(avgReview)
                    .on(avgReview.field("id").isNotNull())
            .where(User.USER.ACTIVE.isTrue())
            .and(Quest.QUEST.ACTIVE.isTrue())
            .and(Quest.QUEST.PRIVATE_QUEST.isFalse())
            .and(Quest.QUEST.DAILY.isFalse())
            .and(Quest.QUEST.ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }

    public List getImagesOfQuest(int id){
        Select<?> query =
            create.select(Image.IMAGE.IMAGE_URL)
            .from(Image.IMAGE)
            .where(Image.IMAGE.QUEST_ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }

    @Transactional(rollbackOn = Exception.class)
    public List createReview(int userId, int questId, com.geoly.app.models.QuestReview questReview){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

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

        if(Integer.parseInt(String.valueOf(result[0])) > 0) return Collections.singletonList(new ResponseEntity<>(StatusMessage.REVIEW_ALREADY_EXIST, HttpStatus.METHOD_NOT_ALLOWED));
        if(Integer.parseInt(String.valueOf(result[1])) < 0) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_PLAY_QUEST, HttpStatus.METHOD_NOT_ALLOWED));

        questReview.setQuest(quest.get());
        questReview.setUser(user.get());
        entityManager.persist(questReview);
        return Collections.singletonList(questReview);
    }

    @Transactional(rollbackOn = Exception.class)
    public List removeReview(int userId ,int questId, int reviewId){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Optional<com.geoly.app.models.QuestReview> questReview = questReviewRepository.findByIdAndUserAndQuest(reviewId, user.get(), quest.get());
        if(!questReview.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.REVIEW_NOT_FOUND, HttpStatus.BAD_REQUEST));

        entityManager.remove(questReview.get());
        return Collections.singletonList(new ResponseEntity<>(StatusMessage.REVIEW_DELETED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List updateReview(int userId, int questId, com.geoly.app.models.QuestReview questReview){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.QuestReview> review = questReviewRepository.findByIdAndUserAndQuest(questReview.getId(), user.get(), quest.get());
        if(!review.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.REVIEW_NOT_FOUND, HttpStatus.BAD_REQUEST));

        questReview.setUser(user.get());
        questReview.setQuest(quest.get());

        entityManager.merge(questReview);
        return Collections.singletonList(questReview);
    }

    @Transactional(rollbackOn = Exception.class)
    public List signUpOnQuest(int userId, int questId){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<List<com.geoly.app.models.Stage>> stage = stageRepository.findAllByQuest(quest.get());
        if(!stage.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.STAGE_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Table<?> stages =
            create.select(Stage.STAGE.ID.as("stage_id"))
            .from(Stage.STAGE)
            .where(Stage.STAGE.QUEST_ID.eq(questId))
            .asTable("stage");

        Select<?> query =
            create.select()
            .from(UserQuest.USER_QUEST, stages)
            .where(UserQuest.USER_QUEST.USER_ID.eq(userId))
            .and(UserQuest.USER_QUEST.STAGE_ID.in(stages.field("stage_id")))
            .and(UserQuest.USER_QUEST.STATUS.notEqual(UserQuestStatus.CANCELED.name()))
            .orderBy(UserQuest.USER_QUEST.ID.desc())
            .limit(1);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);

        if(!q.getResultList().isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_HAS_ACTIVE_QUEST, HttpStatus.METHOD_NOT_ALLOWED));

        com.geoly.app.models.UserQuest userQuest = new com.geoly.app.models.UserQuest();
        userQuest.setStatus(UserQuestStatus.ON_STAGE);
        userQuest.setUser(user.get());
        userQuest.setStage(stage.get().get(0));
        entityManager.persist(userQuest);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_SIGNED_UP_ON_QUEST, HttpStatus.CREATED));
    }

    @Transactional(rollbackOn = Exception.class)
    public List reportQuest(int userId, int questId, QuestReportReason questReportReason){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.QuestReport> report = questReportRepository.findAllByQuestAndUser(quest.get(), user.get());
        if(report.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_REPORT_CREATED, HttpStatus.CREATED));

        com.geoly.app.models.QuestReport questReport = new com.geoly.app.models.QuestReport();
        questReport.setReason(questReportReason);
        questReport.setQuest(quest.get());
        questReport.setUser(user.get());
        entityManager.persist(questReport);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_REPORT_CREATED, HttpStatus.CREATED));
    }
}
