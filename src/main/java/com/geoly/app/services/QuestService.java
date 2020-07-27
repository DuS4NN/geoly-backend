package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.Quest;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.models.*;
import com.geoly.app.repositories.QuestRepository;
import com.geoly.app.repositories.StageRepository;
import com.geoly.app.repositories.UserQuestRepository;
import com.geoly.app.repositories.UserRepository;
import io.sentry.Sentry;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.*;
import java.util.*;

@Service
public class QuestService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private QuestRepository questRepository;
    private StageRepository stageRepository;
    private UserQuestRepository userQuestRepository;

    public QuestService(EntityManager entityManager, DSLContext create, UserRepository userRepository, QuestRepository questRepository, StageRepository stageRepository, UserQuestRepository userQuestRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.stageRepository = stageRepository;
        this.userQuestRepository = userQuestRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public List signInDailyQuest(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        Optional<com.geoly.app.models.Quest> quest = questRepository.findByDaily(true);
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND));
        Optional<com.geoly.app.models.Stage> stage = stageRepository.findByQuest(quest.get());
        if(!stage.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.STAGE_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<UserQuest> alreadyActive = userQuestRepository.findByUserAndStageAndStatus(user.get(), stage.get(), UserQuestStatus.ON_STAGE);
        if(alreadyActive.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_HAS_ACTIVE_QUEST, HttpStatus.METHOD_NOT_ALLOWED));

        UserQuest userQuest = new UserQuest();
        userQuest.setUser(user.get());
        userQuest.setStage(stage.get());
        userQuest.setStatus(UserQuestStatus.ON_STAGE);
        entityManager.persist(userQuest);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_SIGNED_UP_ON_QUEST, HttpStatus.CREATED));
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

    public List getDailyQuest(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Select<?> query =
            create.select(Quest.QUEST.ID.as("questId"), Quest.QUEST.DESCRIPTION, Stage.STAGE.ID.as("stageId"))
            .from(Stage.STAGE)
            .leftJoin(com.geoly.app.jooq.tables.Quest.QUEST)
                .on(com.geoly.app.jooq.tables.Quest.QUEST.ID.eq(Stage.STAGE.QUEST_ID))
            .where(com.geoly.app.jooq.tables.Quest.QUEST.DAILY.isTrue());

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        Coordinates coordinates = new Coordinates();
        coordinates.createFromString(user.get().getAddress());

        Coordinates randomCoordinates = getRandomCoordinates(coordinates.getLatitude(), coordinates.getLongitude(), 2000, userId*100);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowMidnight = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);

        Duration duration = Duration.between(now, tomorrowMidnight);

        result.add(duration.toMillis());
        result.add(randomCoordinates);
        return result;
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
