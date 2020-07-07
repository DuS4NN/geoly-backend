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
import java.util.Optional;

import static org.jooq.impl.DSL.*;


@Service
public class MapService {

    private EntityManager entityManager;
    private DSLContext create;

    public MapService(EntityManager entityManager, DSLContext create){
        this.entityManager = entityManager;
        this.create = create;
    }

    public List getQuestDetailsById(int id){

        Table<?> avgReview =
                create.select(avg(QuestReview.QUEST_REVIEW.REVIEW).as("avg"))
                    .from(QuestReview.QUEST_REVIEW)
                    .where(QuestReview.QUEST_REVIEW.QUEST_ID.eq(id))
                    .asTable("avg_review");

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
                            .groupBy(Stage.STAGE.QUEST_ID)
                            ))
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
                        .and(u1.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
                        .asTable("count_canceled");

        Table<?> coordinates =
                create.select(Stage.STAGE.LATITUDE.as("lat"), Stage.STAGE.LONGITUDE.as("lon"))
                        .from(Stage.STAGE)
                        .where(Stage.STAGE.QUEST_ID.eq(id))
                        .orderBy(Stage.STAGE.ID)
                        .limit(1)
                        .asTable("coordinates");

        Select<?> query =
                create.select(Quest.QUEST.ID, Quest.QUEST.DESCRIPTION, Quest.QUEST.DIFFICULTY, Category.CATEGORY.NAME, Category.CATEGORY.IMAGE_URL, User.USER.NICK_NAME, avgReview.field("avg"), countFinished.field("finished"), countOnStage.field("on_stage"), countCanceled.field("canceled"), coordinates.field("lat"), coordinates.field("lon"))
                    .from(avgReview, countFinished, countOnStage, countCanceled, coordinates, Quest.QUEST)
                    .leftJoin(Category.CATEGORY)
                        .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                    .leftJoin(User.USER)
                        .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
                    .where(Quest.QUEST.ID.eq(id));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        return q.getResultList();
    }
}
