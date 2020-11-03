package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.Log;
import com.geoly.app.models.LogType;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.QuestReviewRepository;
import com.geoly.app.repositories.UserBadgeRepository;
import com.geoly.app.repositories.UserRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.max;

@Service
public class AdminUserService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private UserBadgeRepository userBadgeRepository;
    private QuestReviewRepository questReviewRepository;

    public AdminUserService(EntityManager entityManager, DSLContext create, UserRepository userRepository, UserBadgeRepository userBadgeRepository, QuestReviewRepository questReviewRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.userBadgeRepository = userBadgeRepository;
        this.questReviewRepository = questReviewRepository;
    }

    public Response getUsers(String nick , int page){

        Condition condition = DSL.trueCondition();
        if(!nick.equals("")){
            condition = condition.and(User.USER.NICK_NAME.like("%"+nick+"%"));
        }

        Select<?> query =
            create.select(User.USER.ID, User.USER.NICK_NAME, User.USER.CREATED_AT, User.USER.PROFILE_IMAGE_URL)
                .from(User.USER)
                .where(condition)
                .orderBy(User.USER.CREATED_AT.desc())
                .limit(20)
                .offset((page - 1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List response = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, response);
    }

    public long getUserCounts(){
        return userRepository.count();
    }

    public Response getUser(int id){

        Select<?> badges =
            create.select(UserBadge.USER_BADGE.ID, Badge.BADGE.NAME, Badge.BADGE.IMAGE_URL, UserBadge.USER_BADGE.CREATED_AT)
                .from(UserBadge.USER_BADGE)
                .leftJoin(Badge.BADGE)
                    .on(Badge.BADGE.ID.eq(UserBadge.USER_BADGE.BADGE_ID))
                .where(UserBadge.USER_BADGE.USER_ID.eq(id));

        Query q1 = entityManager.createNativeQuery(badges.getSQL());
        API.setBindParameterValues(q1, badges);
        List badgesResult = q1.getResultList();

        Select<?> createdGroups =
            create.select(Party.PARTY.ID, Party.PARTY.CREATED_AT, Party.PARTY.NAME)
                .from(Party.PARTY)
                .where(Party.PARTY.USER_ID.eq(id));

        Query q2 = entityManager.createNativeQuery(createdGroups.getSQL());
        API.setBindParameterValues(q2, createdGroups);
        List createdGroupsResult = q2.getResultList();

        Select<?> joinedGroups =
            create.select(Party.PARTY.ID, Party.PARTY.NAME, Party.PARTY.USER_ID.as("creator"), User.USER.NICK_NAME, PartyUser.PARTY_USER.CREATED_AT)
                .from(Party.PARTY)
                .leftJoin(PartyUser.PARTY_USER)
                    .on(PartyUser.PARTY_USER.PARTY_ID.eq(Party.PARTY.ID))
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(Party.PARTY.USER_ID))
                .where(PartyUser.PARTY_USER.USER_ID.eq(id))
                .and(Party.PARTY.USER_ID.notEqual(id));

        Query q3 = entityManager.createNativeQuery(joinedGroups.getSQL());
        API.setBindParameterValues(q3, joinedGroups);
        List joinedGroupsResult = q3.getResultList();

        Select<?> createdQuests =
            create.select(Quest.QUEST.ID, Quest.QUEST.CREATED_AT,Quest.QUEST.NAME, Quest.QUEST.ACTIVE)
                .from(Quest.QUEST)
                .where(Quest.QUEST.USER_ID.eq(id));

        Query q4 = entityManager.createNativeQuery(createdQuests.getSQL());
        API.setBindParameterValues(q4, createdQuests);
        List createdQuestsResult = q4.getResultList();

        Select<?> playedQuests =
            create.select(Quest.QUEST.ID, UserQuest.USER_QUEST.UPDATED_AT, Quest.QUEST.NAME, Quest.QUEST.ACTIVE)
                .from(UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                .on(Stage.STAGE.ID.eq(UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(Quest.QUEST)
                .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
                .where(UserQuest.USER_QUEST.USER_ID.eq(id))
                .and(UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                .and(UserQuest.USER_QUEST.STAGE_ID.in(
                        create.select(max(Stage.STAGE.ID))
                                .from(Stage.STAGE)
                                .groupBy(Stage.STAGE.QUEST_ID)));

        Query q5 = entityManager.createNativeQuery(playedQuests.getSQL());
        API.setBindParameterValues(q5, playedQuests);
        List playedQuestsResult = q5.getResultList();


        Select<?> userDetails =
            create.select(User.USER.PROFILE_IMAGE_URL, User.USER.NICK_NAME, User.USER.ABOUT, User.USER.ACTIVE, User.USER.EMAIL, User.USER.ADDRESS, User.USER.VERIFIED, UserOption.USER_OPTION.PRIVATE_PROFILE, UserOption.USER_OPTION.DARK_MODE, UserOption.USER_OPTION.MAP_THEME, UserOption.USER_OPTION.LANGUAGE_ID)
                .from(User.USER)
                .leftJoin(UserOption.USER_OPTION)
                    .on(UserOption.USER_OPTION.USER_ID.eq(User.USER.ID))
                .where(User.USER.ID.eq(id));

        Query q6 = entityManager.createNativeQuery(userDetails.getSQL());
        API.setBindParameterValues(q6, userDetails);
        List userDetailsResult = q6.getResultList();

        Select<?> reviews =
            create.select(QuestReview.QUEST_REVIEW.CREATED_AT, QuestReview.QUEST_REVIEW.REVIEW_TEXT, QuestReview.QUEST_REVIEW.REVIEW, QuestReview.QUEST_REVIEW.QUEST_ID, QuestReview.QUEST_REVIEW.ID)
                .from(QuestReview.QUEST_REVIEW)
                .where(QuestReview.QUEST_REVIEW.USER_ID.eq(id));

        Query q7 = entityManager.createNativeQuery(reviews.getSQL());
        API.setBindParameterValues(q7, reviews);
        List reviewsResult = q7.getResultList();

        List<List> result = new ArrayList<>();
        result.add(userDetailsResult);
        result.add(playedQuestsResult);
        result.add(createdQuestsResult);
        result.add(joinedGroupsResult);
        result.add(createdGroupsResult);
        result.add(badgesResult);
        result.add(reviewsResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response removeBadge(int id, int userId){
        Optional<com.geoly.app.models.UserBadge> userBadge = userBadgeRepository.findById(id);
        if(!userBadge.isPresent()) return new Response(StatusMessage.BADGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);


        JSONObject jo = new JSONObject();
        jo.put("adminId", userId);
        jo.put("userId", userBadge.get().getUser().getId());
        jo.put("badgeId", userBadge.get().getBadge().getId());
        jo.put("userBadgeId", userBadge.get().getId());

        Log log = new Log();
        log.setLogType(LogType.REMOVE_BADGE);
        log.setData(jo.toString());

        entityManager.persist(log);
        entityManager.remove(userBadge.get());

        return new Response(StatusMessage.BADGE_DELETED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response removeReview(int id, int userId){
        Optional<com.geoly.app.models.QuestReview> questReview = questReviewRepository.findById(id);
        if(!questReview.isPresent()) return new Response(StatusMessage.REVIEW_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        JSONObject jo = new JSONObject();
        jo.put("adminId", userId);
        jo.put("userId", questReview.get().getUser().getId());
        jo.put("questId", questReview.get().getQuest().getId());
        jo.put("reviewText", questReview.get().getReviewText());
        jo.put("reviewRating", questReview.get().getReview());
        jo.put("reviewId", questReview.get().getId());

        Log log = new Log();
        log.setLogType(LogType.REMOVE_REVIEW);
        log.setData(jo.toString());

        entityManager.remove(questReview.get());
        entityManager.persist(log);

        return new Response(StatusMessage.REVIEW_DELETED, HttpStatus.ACCEPTED, null);
    }
}
