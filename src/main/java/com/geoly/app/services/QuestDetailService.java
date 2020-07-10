package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.UserQuestStatus;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;

@Service
public class QuestDetailService {

    private EntityManager entityManager;
    private DSLContext create;

    public QuestDetailService(EntityManager entityManager, DSLContext create){
        this.entityManager = entityManager;
        this.create = create;
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

    public void createReview(){

    }

    public void signUpOnQuest(){

    }
}
