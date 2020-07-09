package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.CategoryRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

import static org.jooq.impl.DSL.*;


@Service
public class MapService {

    private EntityManager entityManager;
    private DSLContext create;
    private CategoryRepository categoryRepository;

    public MapService(EntityManager entityManager, DSLContext create, CategoryRepository categoryRepository){
        this.entityManager = entityManager;
        this.create = create;
        this.categoryRepository = categoryRepository;
    }

    public List getQuestDetailsById(int id){

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

        Table<?> coordinates =
                create.select(Stage.STAGE.LATITUDE.as("lat"), Stage.STAGE.LONGITUDE.as("lon"), Stage.STAGE.ID.as("id"))
                        .from(Stage.STAGE)
                        .where(Stage.STAGE.QUEST_ID.eq(id))
                        .orderBy(Stage.STAGE.ID)
                        .limit(1)
                        .asTable("coordinates");

        Select<?> query =
                create.select(Quest.QUEST.ID, Quest.QUEST.DESCRIPTION, Quest.QUEST.DIFFICULTY, Category.CATEGORY.NAME, Category.CATEGORY.IMAGE_URL, User.USER.NICK_NAME, avgReview.field("avg"), countFinished.field("finished"), countOnStage.field("on_stage"), countCanceled.field("canceled"), coordinates.field("lat"), coordinates.field("lon"))
                    .from(countFinished, countOnStage, countCanceled, Quest.QUEST)
                    .leftJoin(Category.CATEGORY)
                        .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                    .leftJoin(User.USER)
                        .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
                    .leftJoin(coordinates)
                        .on(coordinates.field("id").isNotNull())
                    .leftJoin(avgReview)
                        .on(avgReview.field("id").isNotNull())
                    .where(Quest.QUEST.ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }

    public List getAllQuestsByParameters(List<Integer> categoryId, List<Integer> difficulty, List<Integer> review, boolean unreviewed){

        Condition where = DSL.trueCondition();

        if(categoryId != null && !categoryId.isEmpty()){
            where = where.and(Category.CATEGORY.ID.in(categoryId));
        }
        if(difficulty != null && !difficulty.isEmpty() && difficulty.size() == 2){
            where = where.and(Quest.QUEST.DIFFICULTY.between(DSL.coerce(difficulty.get(0), Byte.class)).and(DSL.coerce(difficulty.get(1), Byte.class)));
        }

        Condition having = DSL.trueCondition();

        if(review != null && !review.isEmpty() && review.size() == 2){
            having = having.and(avg(QuestReview.QUEST_REVIEW.REVIEW).between(DSL.coerce(review.get(0), BigDecimal.class)).and(DSL.coerce(review.get(1), BigDecimal.class)));
        }
        if(unreviewed){
           having = having.or(avg(QuestReview.QUEST_REVIEW.REVIEW).isNull());
        }

        Table<?> coordinates =
                create.select(min(Stage.STAGE.ID), Stage.STAGE.LONGITUDE.as("longitude"), Stage.STAGE.LATITUDE.as("latitude"), Stage.STAGE.QUEST_ID)
                .from(Stage.STAGE)
                .groupBy(Stage.STAGE.QUEST_ID)
                .asTable("stage");


        Select<?> query =
            create.select(Quest.QUEST.ID.as("quest_id"), Category.CATEGORY.IMAGE_URL, coordinates.field("latitude"), coordinates.field("longitude"))
                .from(Quest.QUEST)
                .leftJoin(Category.CATEGORY)
                    .on(Category.CATEGORY.ID.eq(Quest.QUEST.ID))
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
                .leftJoin(QuestReview.QUEST_REVIEW)
                    .on(QuestReview.QUEST_REVIEW.QUEST_ID.eq(Quest.QUEST.ID))
                .leftJoin(coordinates)
                    .on(Stage.STAGE.QUEST_ID.eq(Quest.QUEST.ID))
                .where(Quest.QUEST.ACTIVE.isTrue())
                .and(Quest.QUEST.PRIVATE_QUEST.isFalse())
                .and(Quest.QUEST.DAILY.isFalse())
                .and(User.USER.ACTIVE.isTrue())
                .and(where)
                .groupBy(Quest.QUEST.ID)
                .having(having);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }

    public List<com.geoly.app.models.Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
