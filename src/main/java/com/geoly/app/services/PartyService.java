package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.PartyInvateStatus;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.*;
import com.google.common.hash.Hashing;
import com.pusher.rest.Pusher;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.when;

@Service
public class PartyService {

    private EntityManager entityManager;
    private DSLContext create;
    private Pusher pusher;
    private UserRepository userRepository;
    private PartyRepository partyRepository;
    private PartyUserRepository partyUserRepository;
    private PartyInviteRepository partyInviteRepository;
    private QuestRepository questRepository;
    private PartyQuestRepository partyQuestRepository;
    private StageRepository stageRepository;
    private UserPartyQuestRepository userPartyQuestRepository;

    private int PARTY_ON_PAGE = 5;

    public PartyService(EntityManager entityManager, DSLContext create, Pusher pusher, UserRepository userRepository, PartyRepository partyRepository, PartyUserRepository partyUserRepository, PartyInviteRepository partyInviteRepository, QuestRepository questRepository, PartyQuestRepository partyQuestRepository, StageRepository stageRepository, UserPartyQuestRepository userPartyQuestRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.pusher = pusher;
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
        this.partyUserRepository = partyUserRepository;
        this.partyInviteRepository = partyInviteRepository;
        this.questRepository = questRepository;
        this.partyQuestRepository = partyQuestRepository;
        this.stageRepository = stageRepository;
        this.userPartyQuestRepository = userPartyQuestRepository;
    }

    public Response getCreatedParties(int userId, int page){
        Select<?> query =
            create.select(Party.PARTY.ID, Party.PARTY.NAME, Party.PARTY.CREATED_AT)
                .from(Party.PARTY)
                .where(Party.PARTY.USER_ID.eq(userId))
                .orderBy(Party.PARTY.CREATED_AT.desc())
                .limit(PARTY_ON_PAGE)
                .offset((page-1)*PARTY_ON_PAGE);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.NO_PARTY, HttpStatus.NO_CONTENT, null);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public int getCountOfCreatedParties(int userId){
        Select<?> query =
                create.select(count())
                        .from(Party.PARTY)
                        .where(Party.PARTY.USER_ID.eq(userId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        Object result = q.getSingleResult();

        return Integer.parseInt(String.valueOf(result));
    }

    public int getCountOfEnteredParties(int userId){
        Select<?> query =
                create.select(count())
                        .from(PartyUser.PARTY_USER)
                        .leftJoin(Party.PARTY)
                            .on(Party.PARTY.ID.eq(PartyUser.PARTY_USER.PARTY_ID))
                        .where(PartyUser.PARTY_USER.USER_ID.eq(userId))
                        .and(PartyUser.PARTY_USER.USER_ID.notEqual(Party.PARTY.USER_ID));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        Object result = q.getSingleResult();

        return Integer.parseInt(String.valueOf(result));
    }

    @Transactional(rollbackOn = Exception.class)
    public Response deleteParty(int partyId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        int i = partyRepository.deleteByIdAndUser(partyId, user.get());
        if(i == 0){
            return new Response(StatusMessage.GROUP_DOES_NOT_EXIST_OR_USER_IS_NOT_OWNER, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        HashMap<String, Integer> pusherData = new HashMap<>();
        pusherData.put("partyId", partyId);
        pusher.trigger("PARTY-"+partyId, "PARTY-UPDATE", pusherData);

        return new Response(StatusMessage.GROUP_DELETED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response createParty(String name, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        com.geoly.app.models.Party party = new com.geoly.app.models.Party();
        party.setName(name);
        party.setUser(user.get());

        com.geoly.app.models.PartyUser partyUser = new com.geoly.app.models.PartyUser();
        partyUser.setParty(party);
        partyUser.setUser(user.get());

        Set<com.geoly.app.models.PartyUser> set = new HashSet<>();
        set.add(partyUser);
        party.setPartyUser(set);

        entityManager.persist(party);

        return new Response(StatusMessage.GROUP_CREATED, HttpStatus.ACCEPTED, null);
    }

    public Response getPartyQuests(int partyId, int userId){
        Select<?> query =
                create.select(PartyQuest.PARTY_QUEST.ID.as("partyQuestId"), Quest.QUEST.ID.as("questId"), Quest.QUEST.NAME, Category.CATEGORY.IMAGE_URL)
                .from(PartyQuest.PARTY_QUEST)
                .leftJoin(Party.PARTY)
                    .on(Party.PARTY.ID.eq(PartyQuest.PARTY_QUEST.PARTY_ID))
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(PartyQuest.PARTY_QUEST.QUEST_ID))
                .leftJoin(Category.CATEGORY)
                    .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
                .where(Party.PARTY.ID.eq(partyId))
                .and(Party.PARTY.USER_ID.eq(userId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response deleteQuest(int partyId, int questId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, user.get());
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.PartyQuest> partyQuest = partyQuestRepository.findByPartyAndQuest(party.get(), quest.get());
        if(!partyQuest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        entityManager.remove(partyQuest.get());

        HashMap<String, Integer> pusherData = new HashMap<>();
        pusherData.put("partyId", partyId);
        pusherData.put("questId", questId);
        pusher.trigger("QUEST-"+partyId+"-"+questId, "PARTY-QUEST-UPDATE", pusherData);

        return new Response(StatusMessage.QUEST_DELETED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response editGroup(int partyId, int userId, String name){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, user.get());
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        party.get().setName(name);
        party.get().setUser(user.get());
        entityManager.merge(party.get());

        return new Response(StatusMessage.GROUP_EDITED, HttpStatus.ACCEPTED, null);
    }

    public Response getUsersInGroup(int partyId, int userId){
        Select<?> query =
            create.select(com.geoly.app.jooq.tables.User.USER.ID, com.geoly.app.jooq.tables.User.USER.PROFILE_IMAGE_URL, com.geoly.app.jooq.tables.User.USER.NICK_NAME,
                    when(com.geoly.app.jooq.tables.User.USER.ID.eq(userId), 1).otherwise(0))
            .from(PartyUser.PARTY_USER)
            .leftJoin(Party.PARTY)
                .on(Party.PARTY.ID.eq(PartyUser.PARTY_USER.PARTY_ID))
            .leftJoin(com.geoly.app.jooq.tables.User.USER)
                .on(com.geoly.app.jooq.tables.User.USER.ID.eq(PartyUser.PARTY_USER.USER_ID))
            .where(Party.PARTY.USER_ID.eq(userId))
            .and(com.geoly.app.jooq.tables.User.USER.ACTIVE.isTrue())
            .and(Party.PARTY.ID.eq(partyId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response kickUserFromParty(int partyId, int userId, int creatorId){
        Optional<User> creator = userRepository.findById(creatorId);
        if(!creator.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);


        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, creator.get());
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        partyUserRepository.deleteByPartyAndUser(party.get(), user.get());

        HashMap<String, Integer> pusherData = new HashMap<>();
        pusherData.put("partyId", partyId);
        pusherData.put("userId", userId);
        pusher.trigger("PARTY-"+partyId+"-"+userId, "PARTY-USER-UPDATE", pusherData);

        return new Response(StatusMessage.USER_KICKED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response inviteUser(int partyId, String nickName, int userId){
        Optional<User> creator = userRepository.findById(userId);
        if(!creator.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<User> user = userRepository.findByNickName(nickName);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        if(!user.get().isActive()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, creator.get());
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<com.geoly.app.models.PartyUser> partyUser = partyUserRepository.findByUserAndParty(user.get(), party.get());
        if(partyUser.isPresent()) return new Response(StatusMessage.USER_IS_ALREADY_IN_GROUP, HttpStatus.METHOD_NOT_ALLOWED, null);

        Optional<com.geoly.app.models.PartyInvite> partyInviteOptional = partyInviteRepository.findByUserAndPartyAndStatus(user.get(), party.get(), PartyInvateStatus.PENDING);
        if(partyInviteOptional.isPresent()) return new Response(StatusMessage.USER_INVITED, HttpStatus.ACCEPTED, null);

        Select<?> count =
            create.select(count())
                .from(PartyUser.PARTY_USER)
                .leftJoin(com.geoly.app.jooq.tables.User.USER)
                    .on(com.geoly.app.jooq.tables.User.USER.ID.eq(PartyUser.PARTY_USER.USER_ID))
                .where(PartyUser.PARTY_USER.PARTY_ID.eq(partyId))
                .and(com.geoly.app.jooq.tables.User.USER.ACTIVE.isTrue());

        Query q = entityManager.createNativeQuery(count.getSQL());
        API.setBindParameterValues(q, count);
        Object countResult = q.getSingleResult();

        if(Integer.parseInt(String.valueOf(countResult)) >= 10){
            return new Response(StatusMessage.GROUP_IS_FULL, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        com.geoly.app.models.PartyInvite partyInvite = new com.geoly.app.models.PartyInvite();
        partyInvite.setParty(party.get());
        partyInvite.setUser(user.get());
        partyInvite.setStatus(PartyInvateStatus.PENDING);
        partyInvite.setSeen(false);
        entityManager.persist(partyInvite);

        HashMap data = new HashMap();
        data.put("invitationId", partyInvite.getId());
        data.put("partyId", partyId);
        data.put("partyName", party.get().getName());
        data.put("userNick", creator.get().getNickName());
        String id = Hashing.sha256().hashString(user.get().getId()+"", StandardCharsets.UTF_8).toString();
        pusher.trigger("invitations-"+id, "PARTY_INVITE", data);

        return new Response(StatusMessage.USER_INVITED, HttpStatus.ACCEPTED, null);
    }

    public Response getAllCreatedParties(int userId){
        Select<?> query =
                create.select(Party.PARTY.ID, Party.PARTY.NAME)
                        .from(Party.PARTY)
                        .where(Party.PARTY.USER_ID.eq(userId))
                        .orderBy(Party.PARTY.CREATED_AT.desc());

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response addQuest(int partyId, int questId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, user.get());
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.PartyQuest> partyQuestOptional = partyQuestRepository.findByPartyAndQuest(party.get(), quest.get());
        if(partyQuestOptional.isPresent()) return new Response(StatusMessage.QUEST_ALREADY_IN_GROUP, HttpStatus.METHOD_NOT_ALLOWED, null);

        if(quest.get().isPrivateQuest() && quest.get().getUser().getId() != user.get().getId()){
            return new Response(StatusMessage.QUEST_IS_PRIVATE, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        com.geoly.app.models.PartyQuest partyQuest = new com.geoly.app.models.PartyQuest();
        partyQuest.setParty(party.get());
        partyQuest.setQuest(quest.get());
        partyQuest.setActive(true);
        entityManager.persist(partyQuest);

        return new Response(StatusMessage.QUEST_ADDED_TO_GROUP, HttpStatus.ACCEPTED, null);
    }

    public Response getEnteredParties(int userId, int page){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Select<?> query =
            create.select(Party.PARTY.ID, Party.PARTY.NAME, Party.PARTY.USER_ID.as("creator"), PartyUser.PARTY_USER.CREATED_AT)
            .from(Party.PARTY)
            .leftJoin(PartyUser.PARTY_USER)
                .on(PartyUser.PARTY_USER.PARTY_ID.eq(Party.PARTY.ID))
            .leftJoin(com.geoly.app.jooq.tables.User.USER)
                .on(com.geoly.app.jooq.tables.User.USER.ID.eq(Party.PARTY.USER_ID))
            .where(PartyUser.PARTY_USER.USER_ID.eq(userId))
            .and(com.geoly.app.jooq.tables.User.USER.ACTIVE.isTrue())
            .and(Party.PARTY.USER_ID.notEqual(userId))
            .orderBy(PartyUser.PARTY_USER.CREATED_AT.desc())
            .limit(PARTY_ON_PAGE)
            .offset((page-1)*PARTY_ON_PAGE);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.USER_IS_NOT_IN_GROUP, HttpStatus.NO_CONTENT, null);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response leaveParty(int partyId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findById(partyId);
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        if(party.get().getUser().getId() == userId)  return new Response(StatusMessage.CAN_NOT_LEAVE_OWN_GROUP, HttpStatus.METHOD_NOT_ALLOWED, null);

        partyUserRepository.deleteByPartyAndUser(party.get(), user.get());

        HashMap<String, Integer> pusherData = new HashMap<>();
        pusherData.put("partyId", partyId);
        pusherData.put("userId", userId);
        pusher.trigger("PARTY-"+partyId+"-"+userId, "PARTY-USER-UPDATE", pusherData);

        return new Response(StatusMessage.GROUP_LEAVED, HttpStatus.ACCEPTED, null);
    }

    public Response getPartyDetails(int partyId, int userId){
        Select<?> queryDetail =
            create.select(Party.PARTY.NAME, Party.PARTY.CREATED_AT, com.geoly.app.jooq.tables.User.USER.NICK_NAME)
            .from(PartyUser.PARTY_USER)
            .leftJoin(Party.PARTY)
                .on(Party.PARTY.ID.eq(PartyUser.PARTY_USER.PARTY_ID))
            .leftJoin(com.geoly.app.jooq.tables.User.USER)
                .on(com.geoly.app.jooq.tables.User.USER.ID.eq(Party.PARTY.USER_ID))
            .where(PartyUser.PARTY_USER.USER_ID.eq(userId))
            .and(com.geoly.app.jooq.tables.User.USER.ACTIVE.isTrue())
            .and(PartyUser.PARTY_USER.PARTY_ID.eq(partyId));

        Query q1 = entityManager.createNativeQuery(queryDetail.getSQL());
        API.setBindParameterValues(q1, queryDetail);
        List resultDetail = q1.getResultList();

        if(resultDetail.isEmpty()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Select<?> queryUser =
            create.select(com.geoly.app.jooq.tables.User.USER.NICK_NAME, com.geoly.app.jooq.tables.User.USER.PROFILE_IMAGE_URL, PartyUser.PARTY_USER.CREATED_AT)
            .from(PartyUser.PARTY_USER)
            .leftJoin(com.geoly.app.jooq.tables.User.USER)
                .on(com.geoly.app.jooq.tables.User.USER.ID.eq(PartyUser.PARTY_USER.USER_ID))
            .where(PartyUser.PARTY_USER.PARTY_ID.eq(partyId))
            .and(com.geoly.app.jooq.tables.User.USER.ACTIVE.isTrue())
            .orderBy(PartyUser.PARTY_USER.CREATED_AT);

        Query q2 = entityManager.createNativeQuery(queryUser.getSQL());
        API.setBindParameterValues(q2, queryUser);
        List resultUser = q2.getResultList();

        Select<?> queryQuest =
            create.select(Quest.QUEST.DESCRIPTION, Quest.QUEST.DIFFICULTY, Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME, Quest.QUEST.NAME.as("questName"), Quest.QUEST.ID)
            .from(PartyQuest.PARTY_QUEST)
            .leftJoin(Quest.QUEST)
                .on(Quest.QUEST.ID.eq(PartyQuest.PARTY_QUEST.QUEST_ID))
            .leftJoin(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .where(PartyQuest.PARTY_QUEST.PARTY_ID.eq(partyId))
            .and(Quest.QUEST.ACTIVE.isTrue())
            .and(Quest.QUEST.DAILY.isFalse())
            .orderBy(PartyQuest.PARTY_QUEST.ID.desc());

        Query q3 = entityManager.createNativeQuery(queryQuest.getSQL());
        API.setBindParameterValues(q3, queryQuest);
        List resultQuest = q3.getResultList();

        List<List> finalResult = new ArrayList<>();
        finalResult.add(resultDetail);
        finalResult.add(resultUser);
        finalResult.add(resultQuest);

        return new Response(StatusMessage.OK, HttpStatus.OK, finalResult);
    }

    public Response getPartyQuestDetails(int partyId, int questId, int userId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findById(partyId);
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        Optional<com.geoly.app.models.PartyUser> partyUser = partyUserRepository.findByUserAndParty(user.get(), party.get());
        if(!partyUser.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST, null);


        Select<?> stageDetails =
            create.select(Stage.STAGE.ID, Stage.STAGE.TYPE, Stage.STAGE.LATITUDE, Stage.STAGE.LONGITUDE)
                .from(Stage.STAGE)
                .where(Stage.STAGE.QUEST_ID.eq(questId));

        Query q1 = entityManager.createNativeQuery(stageDetails.getSQL());
        API.setBindParameterValues(q1, stageDetails);
        List stageDetailsResult = q1.getResultList();


        Select<?> images =
            create.select(Image.IMAGE.IMAGE_URL)
                .from(Image.IMAGE)
                .where(Image.IMAGE.QUEST_ID.eq(questId));

        Query q4 = entityManager.createNativeQuery(images.getSQL());
        API.setBindParameterValues(q4, images);
        List imagesResult = q4.getResultList();

        Select<?> isActive =
            create.select(count())
                .from(UserPartyQuest.USER_PARTY_QUEST)
                .leftJoin(PartyQuest.PARTY_QUEST)
                    .on(PartyQuest.PARTY_QUEST.ID.eq(UserPartyQuest.USER_PARTY_QUEST.PARTY_QUEST_ID))
                .where(PartyQuest.PARTY_QUEST.QUEST_ID.eq(questId))
                .and(PartyQuest.PARTY_QUEST.PARTY_ID.eq(partyId))
                .and(UserPartyQuest.USER_PARTY_QUEST.USER_ID.eq(userId))
                .and(UserPartyQuest.USER_PARTY_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()));

        Query q2 = entityManager.createNativeQuery(isActive.getSQL());
        API.setBindParameterValues(q2, isActive);
        List isActiveResult = q2.getResultList();

        Select<?> query =
            create.select(UserPartyQuest.USER_PARTY_QUEST.ID, UserPartyQuest.USER_PARTY_QUEST.STATUS, UserPartyQuest.USER_PARTY_QUEST.STAGE_ID, com.geoly.app.jooq.tables.User.USER.NICK_NAME, com.geoly.app.jooq.tables.User.USER.PROFILE_IMAGE_URL,
                    when( UserPartyQuest.USER_PARTY_QUEST.UPDATED_AT.isNull(),  UserPartyQuest.USER_PARTY_QUEST.CREATED_AT).otherwise( UserPartyQuest.USER_PARTY_QUEST.UPDATED_AT))
                .from( UserPartyQuest.USER_PARTY_QUEST)
                .leftJoin(PartyQuest.PARTY_QUEST)
                    .on(PartyQuest.PARTY_QUEST.ID.eq(UserPartyQuest.USER_PARTY_QUEST.PARTY_QUEST_ID))
                .leftJoin(com.geoly.app.jooq.tables.User.USER)
                    .on(com.geoly.app.jooq.tables.User.USER.ID.eq(UserPartyQuest.USER_PARTY_QUEST.USER_ID))
                .where(PartyQuest.PARTY_QUEST.PARTY_ID.eq(partyId))
                .and(PartyQuest.PARTY_QUEST.QUEST_ID.eq(questId))
                .orderBy(UserPartyQuest.USER_PARTY_QUEST.STAGE_ID, when( UserPartyQuest.USER_PARTY_QUEST.UPDATED_AT.isNull(),  UserPartyQuest.USER_PARTY_QUEST.CREATED_AT).otherwise( UserPartyQuest.USER_PARTY_QUEST.UPDATED_AT).desc());

        Query q3 = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q3, query);
        List result = q3.getResultList();


        List<List> finalResult = new ArrayList<>();
        finalResult.add(stageDetailsResult);
        finalResult.add(result);
        finalResult.add(isActiveResult);
        finalResult.add(imagesResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, finalResult);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response signInPartyQuest(int partyId, int questId, int userId){
        //Pouzivate
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        //Quest
        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndDaily(questId, false);
        if(!quest.isPresent())  return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        //Party
        Optional<com.geoly.app.models.Party> party = partyRepository.findById(partyId);
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        //Ci pouzivatel je v party
        Optional<com.geoly.app.models.PartyUser> partyUser = partyUserRepository.findByUserAndParty(user.get(), party.get());
        if(!partyUser.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        //Ci quest je v party
        Optional<com.geoly.app.models.PartyQuest> partyQuest = partyQuestRepository.findByPartyAndQuest(party.get(), quest.get());
        if(!partyQuest.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST, null);



        //Kontrola či userId už nemá aktívny questId v partyId
        Table<?> stages =
            create.select(Stage.STAGE.ID.as("stage_id"))
                .from(Stage.STAGE)
                .where(Stage.STAGE.QUEST_ID.eq(questId))
                .asTable("stages");

        Table<?> stagesMax =
                create.select(max(Stage.STAGE.ID).as("stage_id"))
                        .from(Stage.STAGE)
                        .where(Stage.STAGE.QUEST_ID.eq(questId))
                        .groupBy(Stage.STAGE.QUEST_ID)
                        .asTable("stages");

        Select<?> isOnStage =
            create.select(count())
                .from(UserPartyQuest.USER_PARTY_QUEST, stages)
                .where(UserPartyQuest.USER_PARTY_QUEST.USER_ID.eq(userId))
                .and(UserPartyQuest.USER_PARTY_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
                .and(UserPartyQuest.USER_PARTY_QUEST.STAGE_ID.in(stages.field("stage_id")));

        Query q1 = entityManager.createNativeQuery(isOnStage.getSQL());
        API.setBindParameterValues(q1, isOnStage);
        Object isOnStageResult = q1.getSingleResult();


        Select<?> isFinished =
            create.select(count())
                .from(UserPartyQuest.USER_PARTY_QUEST, stagesMax)
                .where(UserPartyQuest.USER_PARTY_QUEST.USER_ID.eq(userId))
                .and(UserPartyQuest.USER_PARTY_QUEST.STATUS.eq(UserQuestStatus.FINISHED.name()))
                .and(UserPartyQuest.USER_PARTY_QUEST.STAGE_ID.in(stagesMax.field("stage_id")));



        Query q = entityManager.createNativeQuery(isFinished.getSQL());
        API.setBindParameterValues(q, isFinished);
        Object isFinishedResult = q.getSingleResult();


        if(Integer.parseInt(String.valueOf(isOnStageResult)) > 0) return new Response(StatusMessage.USER_HAS_ACTIVE_QUEST, HttpStatus.METHOD_NOT_ALLOWED, null);
        if(Integer.parseInt(String.valueOf(isFinishedResult)) > 0) return new Response(StatusMessage.USER_ALREADY_FINISHED_QUEST, HttpStatus.METHOD_NOT_ALLOWED, null);


        Optional<List<com.geoly.app.models.Stage>> stage = stageRepository.findAllByQuest(quest.get());
        if(!stage.isPresent()) return new Response(StatusMessage.STAGE_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

        com.geoly.app.models.UserPartyQuest userPartyQuest = new com.geoly.app.models.UserPartyQuest();
        userPartyQuest.setPartyQuest(partyQuest.get());
        userPartyQuest.setStage(stage.get().get(0));
        userPartyQuest.setUser(user.get());
        userPartyQuest.setStatus(UserQuestStatus.ON_STAGE);
        entityManager.persist(userPartyQuest);

        return new Response(StatusMessage.USER_SIGNED_UP_ON_QUEST, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response signOutPartyQuest(int partyId, int questId, int userId){
        Select<?> query =
            create.select(UserPartyQuest.USER_PARTY_QUEST.ID)
            .from(UserPartyQuest.USER_PARTY_QUEST)
            .leftJoin(Stage.STAGE)
                .on(Stage.STAGE.ID.eq(UserPartyQuest.USER_PARTY_QUEST.STAGE_ID))
            .leftJoin(PartyQuest.PARTY_QUEST)
                .on(PartyQuest.PARTY_QUEST.ID.eq(UserPartyQuest.USER_PARTY_QUEST.PARTY_QUEST_ID))
            .where(Stage.STAGE.QUEST_ID.eq(questId))
            .and(UserPartyQuest.USER_PARTY_QUEST.USER_ID.eq(userId))
            .and(PartyQuest.PARTY_QUEST.PARTY_ID.eq(partyId))
            .and(UserPartyQuest.USER_PARTY_QUEST.STATUS.eq(UserQuestStatus.ON_STAGE.name()))
            .orderBy(UserPartyQuest.USER_PARTY_QUEST.ID.desc())
            .limit(1);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        Object stageId = q.getSingleResult();

        Optional<com.geoly.app.models.UserPartyQuest> userQuest = userPartyQuestRepository.findById(Integer.parseInt(String.valueOf(stageId)));
        if(!userQuest.isPresent()) return new Response(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        userQuest.get().setStatus(UserQuestStatus.CANCELED);
        entityManager.merge(userQuest.get());

        HashMap<String, Integer> pusherData = new HashMap<>();
        pusherData.put("partyId", partyId);
        pusherData.put("questId", questId);
        pusher.trigger("QUEST-"+partyId+"-"+questId, "PARTY-QUEST-UPDATE", pusherData);

        return new Response(StatusMessage.SIGNED_OUT_OF_QUEST, HttpStatus.ACCEPTED, null);
    }

}
