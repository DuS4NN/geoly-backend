package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.models.UserReportReason;
import com.geoly.app.repositories.UserReportRepository;
import com.geoly.app.repositories.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Service
public class ProfileService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private UserReportRepository userReportRepository;

    public ProfileService(EntityManager entityManager, DSLContext create, UserRepository userRepository, UserReportRepository userReportRepository){
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.userReportRepository = userReportRepository;
    }

    public List getUserDetail(String nickName){
        Select<?> query =
            create.select(UserOption.USER_OPTION.PRIVATE_PROFILE, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, User.USER.ABOUT, User.USER.CREATED_AT)
            .from(User.USER)
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
            .where(User.USER.NICK_NAME.eq(nickName));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.BADGES_EMPTY, HttpStatus.NO_CONTENT));
        return result;
    }

    public List getUserQuests(String nickName){
        Select<?> query =
            create.select(Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME, Quest.QUEST.DIFFICULTY, Quest.QUEST.ID, avg(QuestReview.QUEST_REVIEW.REVIEW))
            .from(Quest.QUEST)
            .leftJoin(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
            .leftJoin(QuestReview.QUEST_REVIEW)
                .on(QuestReview.QUEST_REVIEW.QUEST_ID.eq(Quest.QUEST.ID))
            .where(User.USER.NICK_NAME.eq(nickName))
            .and(Quest.QUEST.ACTIVE.isTrue())
            .and(Quest.QUEST.PRIVATE_QUEST.isFalse())
            .groupBy(Quest.QUEST.ID);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.CREATED_QUESTS_EMPTY, HttpStatus.NO_CONTENT));
        return result;
    }

    public List getUserPlayedQuests(String nickName){
        Select<?> query =
            create.select(Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME, Quest.QUEST.DIFFICULTY, Quest.QUEST.ID, avg(QuestReview.QUEST_REVIEW.REVIEW))
            .from(UserQuest.USER_QUEST)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(UserQuest.USER_QUEST.USER_ID))
            .leftJoin(Stage.STAGE)
                .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
            .leftJoin(Quest.QUEST)
                .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
            .leftJoin(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .leftJoin(QuestReview.QUEST_REVIEW)
                .on(QuestReview.QUEST_REVIEW.QUEST_ID.eq(Quest.QUEST.ID))
            .where(User.USER.NICK_NAME.eq(nickName))
            .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
            .and(UserQuest.USER_QUEST.STAGE_ID.in(
                create.select(max(Stage.STAGE.ID))
                .from(Stage.STAGE)
                .groupBy(Stage.STAGE.QUEST_ID)))
            .groupBy(Quest.QUEST.ID);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.PLAYED_QUESTS_EMPTY, HttpStatus.NO_CONTENT));
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
