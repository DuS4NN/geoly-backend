package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.dao.questSearch;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.StageType;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.CategoryRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    public Response getQuestDetailsById(int id){
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
                    .leftJoin(Stage.STAGE)
                        .on(Stage.STAGE.ID.eq(u1.STAGE_ID))
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
                create.select(Quest.QUEST.ID, Quest.QUEST.NAME.as("questName"), Quest.QUEST.DESCRIPTION, Quest.QUEST.CREATED_AT, Quest.QUEST.DIFFICULTY, Category.CATEGORY.NAME, Category.CATEGORY.IMAGE_URL, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, avgReview.field("avg"), countFinished.field("finished"), countOnStage.field("on_stage"), countCanceled.field("canceled"))
                    .from(countFinished, countOnStage, countCanceled, Quest.QUEST)
                    .leftJoin(Category.CATEGORY)
                        .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                    .leftJoin(User.USER)
                        .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
                    .leftJoin(avgReview)
                        .on(avgReview.field("id").isNotNull())
                    .where(Quest.QUEST.ID.eq(id))
                    .and(Quest.QUEST.ACTIVE.isTrue())
                    .and(User.USER.ACTIVE.isTrue())
                    .and(Quest.QUEST.PRIVATE_QUEST.isFalse())
                    .and(Quest.QUEST.DAILY.isFalse());

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);

        List response = q.getResultList();
        if(response.isEmpty()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        return new Response(StatusMessage.OK, HttpStatus.OK, response);
    }

    public Response getAllQuestByParametersInRadius(questSearch questSearch){

        List<String> stageType = new ArrayList<>(Arrays.asList(questSearch.getStageType()));

        Condition where = DSL.trueCondition();
        if(questSearch.getCategoryId().length > 0){
            List<Integer> list = Arrays.stream(questSearch.getCategoryId()).boxed().collect(Collectors.toList());
            where = where.and(Category.CATEGORY.ID.in(list));
        }

        if(stageType.size()>0){
            if(!stageType.contains(StageType.GO_TO_PLACE.name())){
                stageType.add(StageType.GO_TO_PLACE.name());
            }
            System.out.println(stageType.toString());

            where = where.and(
                (create.select(count().as("count"))
                    .from(Stage.STAGE)
                    .where(Stage.STAGE.QUEST_ID.eq(Quest.QUEST.ID))
                    .and(Stage.STAGE.TYPE.in(stageType)))
                    .having(field("count").eq(stageType.size()))
                    .asField()
                .eq(
                create.select(count())
                    .from(Stage.STAGE)
                    .where(Stage.STAGE.QUEST_ID.eq(Quest.QUEST.ID))
                    .asField()
                )
            );
        }

        where = where.and(Quest.QUEST.DIFFICULTY.between(DSL.coerce(questSearch.getDifficulty()[0], Byte.class)).and(DSL.coerce(questSearch.getDifficulty()[1], Byte.class)));

        Condition having = DSL.trueCondition();
        having = having.and(avg(QuestReview.QUEST_REVIEW.REVIEW).between(DSL.coerce(questSearch.getReview()[0], BigDecimal.class)).and(DSL.coerce(questSearch.getReview()[1], BigDecimal.class)));

        if(questSearch.isUnreviewed()){
           having = having.or(avg(QuestReview.QUEST_REVIEW.REVIEW).isNull());
        }

        Table<?> coordinates =
            create.select(min(Stage.STAGE.ID), Stage.STAGE.LONGITUDE.as("longitude"), Stage.STAGE.LATITUDE.as("latitude"), Stage.STAGE.QUEST_ID)
            .from(Stage.STAGE)
            .groupBy(Stage.STAGE.QUEST_ID)
            .having(Stage.STAGE.LONGITUDE.between(DSL.coerce(questSearch.getCoordinatesNw()[0], Double.class)).and(DSL.coerce(questSearch.getCoordinatesNw()[1], Double.class)))
            .and(Stage.STAGE.LATITUDE.between(DSL.coerce(questSearch.getCoordinatesSe()[0], Double.class)).and(DSL.coerce(questSearch.getCoordinatesSe()[1], Double.class)))
            .asTable("stage");

        Select<?> query =
            create.select(Quest.QUEST.ID.as("quest_id"), Category.CATEGORY.IMAGE_URL, coordinates.field("latitude"), coordinates.field("longitude"))
                .from(Quest.QUEST)
                .leftJoin(Category.CATEGORY)
                    .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(Quest.QUEST.USER_ID))
                .leftJoin(QuestReview.QUEST_REVIEW)
                    .on(QuestReview.QUEST_REVIEW.QUEST_ID.eq(Quest.QUEST.ID))
                .rightJoin(coordinates)
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

        List response = q.getResultList();
        if(response.isEmpty()) return new Response(StatusMessage.QUESTS_WITH_PARAMETERS_NOT_FOUND, HttpStatus.NO_CONTENT, null);
        return new Response(StatusMessage.OK, HttpStatus.OK, response);
    }

    public List<com.geoly.app.models.Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
