package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.models.UserReportReason;
import com.geoly.app.repositories.UserReportRepository;
import com.geoly.app.repositories.UserRepository;
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
public class ProfileService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private UserReportRepository userReportRepository;

    private int QUESTS_ON_PAGE = 5;

    public ProfileService(EntityManager entityManager, DSLContext create, UserRepository userRepository, UserReportRepository userReportRepository){
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.userReportRepository = userReportRepository;
    }

    public List getCreatedCount(String nickName){
        Select<?> query =
            create.select(count())
                .from(Quest.QUEST)
                .leftJoin(User.USER)
                .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
                .where(User.USER.NICK_NAME.eq(nickName))
                .and(Quest.QUEST.DAILY.isFalse())
                .and(Quest.QUEST.ACTIVE.isTrue())
                .and(Quest.QUEST.PRIVATE_QUEST.isFalse());

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        return result;
    }

    public List getFinishedCount(String nickName){
        Select<?> query =
                create.select(count())
                        .from(UserQuest.USER_QUEST)
                        .leftJoin(User.USER)
                        .on(User.USER.ID.eq(UserQuest.USER_QUEST.USER_ID))
                        .leftJoin(Stage.STAGE)
                            .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                        .leftJoin(Quest.QUEST)
                        .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
                        .where(User.USER.NICK_NAME.eq(nickName))
                        .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                        .and(Quest.QUEST.DAILY.isFalse())
                        .and(UserQuest.USER_QUEST.STAGE_ID.in(
                                create.select(max(Stage.STAGE.ID))
                                        .from(Stage.STAGE)
                                        .groupBy(Stage.STAGE.QUEST_ID)));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        return result;
    }

    public List getUserDetail(String nickName, int userId){

        Table<?> bestSeason =
            create.select(sum(Point.POINT.AMOUNT).as("bestpoints"))
                .from(Point.POINT)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(Point.POINT.USER_ID))
                .where(User.USER.NICK_NAME.eq(nickName))
                .groupBy(concat(month(Point.POINT.CREATED_AT), year(Point.POINT.CREATED_AT), Point.POINT.USER_ID))
                .asTable("bestSeason");

        Table<?> thisSeason =
            create.select(sum(Point.POINT.AMOUNT).as("points"))
                    .from(Point.POINT)
                    .leftJoin(User.USER)
                        .on(User.USER.ID.eq(Point.POINT.USER_ID))
                    .where(User.USER.NICK_NAME.eq(nickName))
                    .and(year(Point.POINT.CREATED_AT).eq(year(currentDate())))
                    .and((month(Point.POINT.CREATED_AT).eq(month(currentDate()))))
                    .groupBy(Point.POINT.USER_ID)
                    .asTable("thisSeason");

        Select<?> query =
            create.select(User.USER.ID, UserOption.USER_OPTION.PRIVATE_PROFILE, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, User.USER.ABOUT, User.USER.CREATED_AT, max(bestSeason.field("bestpoints")), thisSeason.field("points"),
                    when(User.USER.ID.eq(userId), 1).otherwise(0))
            .from(bestSeason, thisSeason, User.USER)
            .leftJoin(UserOption.USER_OPTION)
                .on(UserOption.USER_OPTION.USER_ID.eq(User.USER.ID))
            .where(User.USER.NICK_NAME.eq(nickName))
            .and(User.USER.ACTIVE.isTrue());

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }

    public List getUserBadges(String nickName){
        Select<?> query =
            create.select(Badge.BADGE.IMAGE_URL, Badge.BADGE.NAME, UserBadge.USER_BADGE.CREATED_AT)
            .from(UserBadge.USER_BADGE)
            .leftJoin(Badge.BADGE)
                .on(Badge.BADGE.ID.eq(UserBadge.USER_BADGE.BADGE_ID))
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(UserBadge.USER_BADGE.USER_ID))
            .where(User.USER.NICK_NAME.eq(nickName))
            .orderBy(UserBadge.USER_BADGE.CREATED_AT.desc());

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.BADGES_EMPTY, HttpStatus.NO_CONTENT));
        return result;
    }

    public List getUserQuests(String nickName, int page){
        Select<?> query =
            create.select(Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME, Quest.QUEST.DIFFICULTY, Quest.QUEST.ID, User.USER.NICK_NAME.as("userName"), User.USER.PROFILE_IMAGE_URL, Quest.QUEST.CREATED_AT, Quest.QUEST.NAME.as("questName"))
            .from(Quest.QUEST)
            .leftJoin(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
            .where(User.USER.NICK_NAME.eq(nickName))
            .and(Quest.QUEST.ACTIVE.isTrue())
            .and(Quest.QUEST.PRIVATE_QUEST.isFalse())
            .and(Quest.QUEST.DAILY.isFalse())
            .limit(QUESTS_ON_PAGE)
            .offset((page-1)*QUESTS_ON_PAGE);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        return result;
    }

    public List getUserActivity(String nickName){
        Select<?> query =
            create.select(UserQuest.USER_QUEST.UPDATED_AT, count())
                .from(UserQuest.USER_QUEST)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(UserQuest.USER_QUEST.USER_ID))
                .where(User.USER.NICK_NAME.eq(nickName))
                    .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                    .and(UserQuest.USER_QUEST.STAGE_ID.in(
                        create.select(max(Stage.STAGE.ID))
                                .from(Stage.STAGE)
                                .groupBy(Stage.STAGE.QUEST_ID)))
                .groupBy(date(UserQuest.USER_QUEST.UPDATED_AT));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        return result;
    }

    public List getUserPlayedQuests(String nickName, int page){
        Select<?> query =
            create.select(Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME, Quest.QUEST.DIFFICULTY, Quest.QUEST.ID, User.USER.NICK_NAME.as("userName"), User.USER.PROFILE_IMAGE_URL, UserQuest.USER_QUEST.UPDATED_AT, Quest.QUEST.NAME.as("questName"))
            .from(UserQuest.USER_QUEST)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(UserQuest.USER_QUEST.USER_ID))
            .leftJoin(Stage.STAGE)
                .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
            .leftJoin(Quest.QUEST)
                .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
            .leftJoin(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .where(User.USER.NICK_NAME.eq(nickName))
            .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
            .and(Quest.QUEST.DAILY.isFalse())
            .and(UserQuest.USER_QUEST.STAGE_ID.in(
                create.select(max(Stage.STAGE.ID))
                .from(Stage.STAGE)
                .groupBy(Stage.STAGE.QUEST_ID)))
            .limit(QUESTS_ON_PAGE)
            .offset((page-1)*QUESTS_ON_PAGE);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public List reportUser(String nickName, UserReportReason reason, int userId){
        Optional<com.geoly.app.models.User> reported = userRepository.findByNickName(nickName);
        if(!reported.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));
        Optional<com.geoly.app.models.User> complainant = userRepository.findById(userId);
        if(!complainant.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.UserReport> report = userReportRepository.findAllByUserComplainantAndUserReported(complainant.get(), reported.get());
        if(report.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_REPORT_CREATED, HttpStatus.CREATED));

        com.geoly.app.models.UserReport userReport = new com.geoly.app.models.UserReport();
        userReport.setReason(reason);
        userReport.setUserComplainant(complainant.get());
        userReport.setUserReported(reported.get());
        entityManager.merge(userReport);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_REPORT_CREATED, HttpStatus.CREATED));
    }
}
