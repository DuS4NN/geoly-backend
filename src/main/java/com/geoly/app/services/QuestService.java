package com.geoly.app.services;

import com.geoly.app.config.API;

import com.geoly.app.dao.EditQuest;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Quest;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.models.*;
import com.geoly.app.repositories.*;
import com.tinify.Source;
import com.tinify.Tinify;
import io.sentry.Sentry;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.File;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private QuestRepository questRepository;
    private StageRepository stageRepository;
    private UserQuestRepository userQuestRepository;
    private CategoryRepository categoryRepository;
    private ImageRepository imageRepository;

    private int QUESTS_ON_PAGE = 5;

    public QuestService(EntityManager entityManager, DSLContext create, UserRepository userRepository, QuestRepository questRepository, StageRepository stageRepository, UserQuestRepository userQuestRepository, CategoryRepository categoryRepository, ImageRepository imageRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.stageRepository = stageRepository;
        this.userQuestRepository = userQuestRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    public Response getActiveQuest(int userId){
        Select<?> query =
            create.select(Stage.STAGE.QUEST_ID.as("quest_id"), Quest.QUEST.NAME.as("quest_name"), com.geoly.app.jooq.tables.Category.CATEGORY.NAME, com.geoly.app.jooq.tables.Category.CATEGORY.IMAGE_URL)
                .from(com.geoly.app.jooq.tables.UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
                .leftJoin(com.geoly.app.jooq.tables.Category.CATEGORY)
                    .on(com.geoly.app.jooq.tables.Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                .where(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.USER_ID.eq(userId))
                .and(Quest.QUEST.DAILY.isFalse())
                .and(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
                .orderBy(Stage.STAGE.QUEST_ID, Stage.STAGE.ID);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.ACTIVE_QUESTS_NOT_FOUND, HttpStatus.NO_CONTENT, null);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getAllCreatedQuests(int userId, int page){
        Select<?> query =
                create.select(Quest.QUEST.ID, Quest.QUEST.CREATED_AT, Quest.QUEST.DESCRIPTION, Quest.QUEST.DIFFICULTY, Quest.QUEST.PRIVATE_QUEST, com.geoly.app.jooq.tables.Category.CATEGORY.NAME, com.geoly.app.jooq.tables.Category.CATEGORY.IMAGE_URL, Quest.QUEST.NAME.as("questName"))
                    .from(Quest.QUEST)
                    .leftJoin(com.geoly.app.jooq.tables.Category.CATEGORY)
                        .on(com.geoly.app.jooq.tables.Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                    .where(Quest.QUEST.USER_ID.eq(userId))
                    .and(Quest.QUEST.ACTIVE.isTrue())
                    .and(Quest.QUEST.DAILY.isFalse())
                    .orderBy(Quest.QUEST.CREATED_AT.desc())
                    .limit(QUESTS_ON_PAGE)
                    .offset((page-1)*QUESTS_ON_PAGE);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.CREATED_QUESTS_NOT_FOUND, HttpStatus.NO_CONTENT, null);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public int getCountCreatedQuests(int userId){
        Select<?> query =
                create.select(DSL.count())
                        .from(Quest.QUEST)
                        .where(Quest.QUEST.USER_ID.eq(userId))
                        .and(Quest.QUEST.ACTIVE.isTrue())
                        .and(Quest.QUEST.DAILY.isFalse());

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        Object result = q.getSingleResult();

        return Integer.parseInt(String.valueOf(result));
    }

    @Transactional(rollbackOn = Exception.class)
    public Response editQuestImage(List<MultipartFile> files, int userId, int questId, int[] deleted) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndUser(questId, user.get());
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);


        for(int deletedImageId : deleted){
           Optional<Image> image = imageRepository.findByIdAndQuest(deletedImageId, quest.get());
           if(image.isPresent()){
               File dir = new File(image.get().getImageUrl());
               if(dir.exists()){
                   dir.delete();
               }

               entityManager.remove(image.get());
           }
        }

        File dir = new File(API.questImageUrl+questId);
        if(!dir.exists()){
            dir.mkdirs();
        }

        for(MultipartFile file : files){

            long imageName = System.currentTimeMillis();

            Source source = Tinify.fromBuffer(file.getBytes());
            source.toFile(API.questImageUrl+questId+"/"+imageName+".jpg");

            Image image = new Image();
            image.setQuest(quest.get());
            image.setImageUrl(API.questImageUrl+questId+"/"+imageName+".jpg");
            entityManager.persist(image);
        }

        return new Response(StatusMessage.IMAGES_SAVED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response editQuestDetails(EditQuest questDetails, int questId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndUser(questId, user.get());
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<Category> category = categoryRepository.findById(questDetails.getCategoryId());
        if(!category.isPresent()) return new Response(StatusMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        quest.get().setPrivateQuest(questDetails.isPrivateQuest());
        quest.get().setCategory(category.get());
        quest.get().setName(questDetails.getName());
        quest.get().setDescription(questDetails.getDescription());
        quest.get().setDifficulty(questDetails.getDifficulty());
        entityManager.merge(quest.get());

        return new Response(StatusMessage.QUEST_EDITED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response doActionWithQuest(int questId, int userId, String action){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndUser(questId, user.get());
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        switch (action){
            case "DISABLE":
                quest.get().setActive(false);
                entityManager.merge(quest.get());
                return new Response(StatusMessage.QUEST_DISABLED, HttpStatus.ACCEPTED, null);
            case "ACTIVATE":
                quest.get().setActive(true);
                entityManager.merge(quest.get());
                return new Response(StatusMessage.QUEST_ACTIVATED, HttpStatus.ACCEPTED, null);
            case "DELETE":
                entityManager.remove(quest.get());
                return new Response(StatusMessage.QUEST_DELETED, HttpStatus.ACCEPTED, null);
        }
        return new Response(StatusMessage.INVALID_ACTION, HttpStatus.BAD_REQUEST, null);
    }

    public Response getQuestImagesForEdit(int questId, int userId){
        Select<?> query =
            create.select(com.geoly.app.jooq.tables.Image.IMAGE.IMAGE_URL, com.geoly.app.jooq.tables.Image.IMAGE.ID)
            .from(com.geoly.app.jooq.tables.Image.IMAGE)
            .leftJoin(Quest.QUEST)
                .on(Quest.QUEST.ID.eq(com.geoly.app.jooq.tables.Image.IMAGE.QUEST_ID))
            .where(com.geoly.app.jooq.tables.Image.IMAGE.QUEST_ID.eq(questId))
            .and(Quest.QUEST.USER_ID.eq(userId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.NO_IMAGES, HttpStatus.NO_CONTENT, null);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public int getCountPlayedQuests(int userId){
        Select<?> query =
            create.select(DSL.countDistinct(Stage.STAGE.QUEST_ID))
                .from(com.geoly.app.jooq.tables.UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
                .where(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.USER_ID.eq(userId))
                .and(Quest.QUEST.DAILY.isFalse());

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        Object result = q.getSingleResult();

        return Integer.parseInt(String.valueOf(result));
    }

    public Response getAllPlayedQuests(int userId, int page){

        Table<?> questOnPage =
            create.select(Stage.STAGE.QUEST_ID.as("questOnPage_ID"))
                .from(com.geoly.app.jooq.tables.UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
                .where(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.USER_ID.eq(userId))
                .and(Quest.QUEST.DAILY.isFalse())
                .groupBy(Stage.STAGE.QUEST_ID)
                .orderBy(DSL.max(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.ID).desc())
                .limit(QUESTS_ON_PAGE)
                .offset((page-1)*QUESTS_ON_PAGE)
                .asTable("questsOnPage");

        Select<?> query =
            create.select(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.STATUS, Stage.STAGE.ID.as("stage_id"), Stage.STAGE.TYPE, Stage.STAGE.QUEST_ID.as("quest_id"), Quest.QUEST.NAME.as("quest_name"), com.geoly.app.jooq.tables.Category.CATEGORY.IMAGE_URL, com.geoly.app.jooq.tables.UserQuest.USER_QUEST.UPDATED_AT, com.geoly.app.jooq.tables.Category.CATEGORY.NAME.as("category_name"))
                .from(questOnPage, com.geoly.app.jooq.tables.UserQuest.USER_QUEST)
                .leftJoin(Stage.STAGE)
                    .on(Stage.STAGE.ID.eq(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.STAGE_ID))
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
                .leftJoin(com.geoly.app.jooq.tables.Category.CATEGORY)
                    .on(com.geoly.app.jooq.tables.Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                .where(Quest.QUEST.ID.in(questOnPage.field("questOnPage_ID")))
                .orderBy(com.geoly.app.jooq.tables.UserQuest.USER_QUEST.ID.desc());


        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.PLAYED_QUESTS_EMPTY, HttpStatus.NO_CONTENT, null);

        Collections.reverse(result);

        List<List<Object[]>> finalResult = new ArrayList<>();
        List<Object[]> oneQuest = new ArrayList<>();

        Object[] firstStage = (Object[]) result.get(0);
        int lastId = Integer.parseInt(String.valueOf(firstStage[3]));

        for(int i=0; i<result.size(); i++){
            Object[] array = (Object[]) result.get(i);

            if(Integer.parseInt(String.valueOf(array[3])) != lastId){
                lastId = Integer.parseInt(String.valueOf(array[3]));
                Collections.reverse(oneQuest);
                finalResult.add(oneQuest.stream().collect(Collectors.toList()));
                oneQuest.clear();
            }
            oneQuest.add(array);

        }
        finalResult.add(oneQuest);

        return new Response(StatusMessage.OK, HttpStatus.OK, finalResult);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response signInDailyQuest(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST,null);

        if(user.get().getAddress() == null){
            return new Response(StatusMessage.USER_ADDRESS_NULL, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        Optional<com.geoly.app.models.Quest> quest = questRepository.findByDaily(true);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Stage> stage = stageRepository.findByQuest(quest.get());
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<UserQuest> alreadyActive = userQuestRepository.findByUserAndStageAndStatus(user.get(), stage.get(), UserQuestStatus.ON_STAGE);
        if(alreadyActive.isPresent()) return new Response(StatusMessage.USER_HAS_ACTIVE_DAILY_QUEST, HttpStatus.METHOD_NOT_ALLOWED, null);

        UserQuest userQuest = new UserQuest();
        userQuest.setUser(user.get());
        userQuest.setStage(stage.get());
        userQuest.setStatus(UserQuestStatus.ON_STAGE);
        entityManager.persist(userQuest);

        return new Response(StatusMessage.USER_SIGNED_UP_ON_QUEST, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    @Scheduled(cron = "0 0 * * * *")
    public void resetDailyQuests(){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findByDaily(true);
        if(!quest.isPresent()){
            Sentry.capture("Daily quest not found.");
            return;
        }
        Optional<List<com.geoly.app.models.Stage>> stage = stageRepository.findAllByQuest(quest.get());
        if(!stage.isPresent() || stage.get().isEmpty()){
            Sentry.capture("Daily quest stage not found.");
            return;
        }

        Optional<List<UserQuest>> userQuests = userQuestRepository.findAllByStageAndStatus(stage.get().get(0), UserQuestStatus.ON_STAGE);
        if(!userQuests.isPresent() || userQuests.get().isEmpty()){
            return;
        }

        for(UserQuest userQuest : userQuests.get()){
            userQuest.setStatus(UserQuestStatus.CANCELED);
            entityManager.merge(userQuest);
        }
    }

    public Response getDailyQuest(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        if(user.get().getAddress() == null){
            return new Response(StatusMessage.USER_ADDRESS_NULL, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        Select<?> query =
            create.select(Quest.QUEST.ID.as("questId"), Quest.QUEST.DESCRIPTION, Stage.STAGE.ID.as("stageId"))
            .from(Stage.STAGE)
            .leftJoin(com.geoly.app.jooq.tables.Quest.QUEST)
                .on(com.geoly.app.jooq.tables.Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
            .where(com.geoly.app.jooq.tables.Quest.QUEST.DAILY.isTrue());

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        Coordinates coordinates = new Coordinates();
        coordinates.createFromString(user.get().getAddress());

        Coordinates randomCoordinates = getRandomCoordinates(coordinates.getLatitude(), coordinates.getLongitude(), 2000, userId*100);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowMidnight = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);

        Duration duration = Duration.between(now, tomorrowMidnight);

        result.add(duration.toMillis());
        result.add(randomCoordinates);
        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    private Coordinates getRandomCoordinates(double lat, double lon, int radius, int userId){
        double degree = 6371 * 2 * Math.PI / 360 * 1000;
        double[] rr = getRandomPoint(radius, getSeedFromDate(userId));

        double random_lat = lat + rr[0] / degree;
        double random_lon = lon + rr[1] / (degree * Math.cos(lat * Math.PI / 180));

        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(random_lat);
        coordinates.setLongitude(random_lon);

        return coordinates;
    }

    private double[] getRandomPoint(int radius, float seed){
        float r = (float) (radius * Math.pow(seed,0.5));
        double theta = seed * 2 * Math.PI;
        double[] randomPoints = {r * Math.cos(theta), r * Math.sin(theta)};

        return randomPoints;
    }

    private float getSeedFromDate(int userId){
        LocalDate date = LocalDate.now();
        int seed = date.getYear() + date.getMonthValue() + date.getDayOfMonth() + userId;
        Random random = new Random(seed);

        return random.nextFloat();
    }
}
