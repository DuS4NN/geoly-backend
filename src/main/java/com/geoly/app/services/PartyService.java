package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.PartyInvateStatus;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserQuestStatus;
import com.geoly.app.repositories.*;
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

@Service
public class PartyService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private PartyRepository partyRepository;
    private PartyUserRepository partyUserRepository;
    private PartyInviteRepository partyInviteRepository;
    private QuestRepository questRepository;
    private PartyQuestRepository partyQuestRepository;
    private StageRepository stageRepository;
    private UserPartyQuestRepository userPartyQuestRepository;

    public PartyService(EntityManager entityManager, DSLContext create, UserRepository userRepository, PartyRepository partyRepository, PartyUserRepository partyUserRepository, PartyInviteRepository partyInviteRepository, QuestRepository questRepository, PartyQuestRepository partyQuestRepository, StageRepository stageRepository, UserPartyQuestRepository userPartyQuestRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
        this.partyUserRepository = partyUserRepository;
        this.partyInviteRepository = partyInviteRepository;
        this.questRepository = questRepository;
        this.partyQuestRepository = partyQuestRepository;
        this.stageRepository = stageRepository;
        this.userPartyQuestRepository = userPartyQuestRepository;
    }

    public List getAllParties(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Select<?> query =
            create.select(Party.PARTY.ID, Party.PARTY.NAME, Party.PARTY.USER_ID.as("creator"), PartyUser.PARTY_USER.CREATED_AT)
            .from(Party.PARTY)
            .leftJoin(PartyUser.PARTY_USER)
                .on(PartyUser.PARTY_USER.PARTY_ID.eq(Party.PARTY.ID))
            .where(PartyUser.PARTY_USER.USER_ID.eq(userId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_IS_NOT_IN_GROUP, HttpStatus.NO_CONTENT));

        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public List leaveParty(int partyId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.Party> party = partyRepository.findById(partyId);
        if(!party.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));

        if(party.get().getUser().getId() == userId)  return Collections.singletonList(new ResponseEntity<>(StatusMessage.CAN_NOT_LEAVE_OWN_GROUP, HttpStatus.METHOD_NOT_ALLOWED));

        partyUserRepository.deleteByPartyAndUser(party.get(), user.get());

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_LEAVED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List deleteParty(int partyId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        int i = partyRepository.deleteByIdAndUser(partyId, user.get());
        if(i == 0){
            return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_DOES_NOT_EXIST_OR_USER_IS_NOT_OWNER, HttpStatus.OK));
        }

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_DELETED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List createParty(String name, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

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

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_CREATED, HttpStatus.OK));
    }

    public List getPartyDetails(int partyId, int userId){
        Select<?> queryDetail =
            create.select(com.geoly.app.jooq.tables.User.USER.NICK_NAME, Party.PARTY.USER_ID, Party.PARTY.NAME, Party.PARTY.CREATED_AT)
            .from(PartyUser.PARTY_USER)
            .leftJoin(Party.PARTY)
                .on(Party.PARTY.ID.eq(PartyUser.PARTY_USER.PARTY_ID))
            .leftJoin(com.geoly.app.jooq.tables.User.USER)
                .on(com.geoly.app.jooq.tables.User.USER.ID.eq(Party.PARTY.USER_ID))
            .where(PartyUser.PARTY_USER.USER_ID.eq(userId))
            .and(PartyUser.PARTY_USER.PARTY_ID.eq(partyId));

        Query q1 = entityManager.createNativeQuery(queryDetail.getSQL());
        GeolyAPI.setBindParameterValues(q1, queryDetail);
        List resultDetail = q1.getResultList();

        if(resultDetail.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));

        Select<?> queryUser =
            create.select(com.geoly.app.jooq.tables.User.USER.NICK_NAME, com.geoly.app.jooq.tables.User.USER.PROFILE_IMAGE_URL, com.geoly.app.jooq.tables.User.USER.ID, PartyUser.PARTY_USER.CREATED_AT)
            .from(PartyUser.PARTY_USER)
            .leftJoin(com.geoly.app.jooq.tables.User.USER)
                .on(com.geoly.app.jooq.tables.User.USER.ID.eq(PartyUser.PARTY_USER.USER_ID))
            .where(PartyUser.PARTY_USER.PARTY_ID.eq(partyId));

        Query q2 = entityManager.createNativeQuery(queryUser.getSQL());
        GeolyAPI.setBindParameterValues(q2, queryUser);
        List resultUser = q2.getResultList();

        Select<?> queryQuest =
            create.select(PartyQuest.PARTY_QUEST.ACTIVE, Quest.QUEST.DESCRIPTION, Quest.QUEST.DIFFICULTY, Category.CATEGORY.IMAGE_URL, Category.CATEGORY.NAME)
            .from(PartyQuest.PARTY_QUEST)
            .leftJoin(Quest.QUEST)
                .on(Quest.QUEST.ID.eq(PartyQuest.PARTY_QUEST.QUEST_ID))
            .leftJoin(Category.CATEGORY)
                    .on(Category.CATEGORY.ID.eq(Quest.QUEST.CATEGORY_ID))
            .where(PartyQuest.PARTY_QUEST.PARTY_ID.eq(partyId))
            .and(Quest.QUEST.ACTIVE.isTrue())
            .and(Quest.QUEST.DAILY.isFalse());

        Query q3 = entityManager.createNativeQuery(queryQuest.getSQL());
        GeolyAPI.setBindParameterValues(q3, queryQuest);
        List resultQuest = q3.getResultList();

        List<List> finalResult = new ArrayList<>();
        finalResult.add(resultDetail);
        finalResult.add(resultUser);
        finalResult.add(resultQuest);

        return finalResult;
    }

    @Transactional(rollbackOn = Exception.class)
    public List kickUserFromParty(int partyId, int userId, int creatorId){
        Optional<User> creator = userRepository.findById(creatorId);
        if(!creator.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));


        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, creator.get());
        if(!party.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));

        partyUserRepository.deleteByPartyAndUser(party.get(), user.get());

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_KICKED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List inviteUser(int partyId, String nickName, int userId){
        Optional<User> creator = userRepository.findById(userId);
        if(!creator.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<User> user = userRepository.findByNickName(nickName);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, creator.get());
        if(!party.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.PartyUser> partyUser = partyUserRepository.findByUserAndParty(user.get(), party.get());
        if(partyUser.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_IS_ALREADY_IN_GROUP, HttpStatus.METHOD_NOT_ALLOWED));

        Optional<com.geoly.app.models.PartyInvite> partyInviteOptional = partyInviteRepository.findByUserAndPartyAndStatus(user.get(), party.get(), PartyInvateStatus.PENDING);
        if(partyInviteOptional.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_INVITED, HttpStatus.OK));

        com.geoly.app.models.PartyInvite partyInvite = new com.geoly.app.models.PartyInvite();
        partyInvite.setParty(party.get());
        partyInvite.setUser(user.get());
        partyInvite.setStatus(PartyInvateStatus.PENDING);
        entityManager.persist(partyInvite);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_INVITED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List addQuest(int partyId, int questId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, user.get());
        if(!party.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndUser(questId, user.get());
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.PartyQuest> partyQuestOptional = partyQuestRepository.findByPartyAndQuest(party.get(), quest.get());
        if(partyQuestOptional.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_ALREADY_IN_GROUP, HttpStatus.METHOD_NOT_ALLOWED));

        com.geoly.app.models.PartyQuest partyQuest = new com.geoly.app.models.PartyQuest();
        partyQuest.setParty(party.get());
        partyQuest.setQuest(quest.get());
        partyQuest.setActive(true);
        entityManager.persist(partyQuest);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_ADDED_TO_GROUP, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List changeActiveQuest(int partyId, int questId, int userId, String operation){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.Party> party = partyRepository.findByIdAndUser(partyId, user.get());
        if(!party.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndUser(questId, user.get());
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.PartyQuest> partyQuest = partyQuestRepository.findByPartyAndQuest(party.get(), quest.get());
        if(!partyQuest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND));

        if(operation.equals("ACTIVATE")){
            partyQuest.get().setActive(true);
        }else{
            partyQuest.get().setActive(false);
        }
        entityManager.merge(partyQuest.get());
        return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_STATE_CHANGED, HttpStatus.NOT_FOUND));

    }

    @Transactional(rollbackOn = Exception.class)
    public List signInPartyQuest(int partyId, int questId, int userId){
        //Kontrola či existuje skupina partyId, v ktorej sa nachádza používateľ userId, v ktorej je quest questId
        Select<?> query =
            create.select(Party.PARTY.ID)
            .from(Party.PARTY)
            .leftJoin(PartyUser.PARTY_USER)
                .on(PartyUser.PARTY_USER.PARTY_ID.eq(Party.PARTY.ID))
            .leftJoin(PartyQuest.PARTY_QUEST)
                .on(PartyQuest.PARTY_QUEST.PARTY_ID.eq(Party.PARTY.ID))
            .where(Party.PARTY.ID.eq(partyId))
            .and(PartyUser.PARTY_USER.USER_ID.eq(userId))
            .and(PartyQuest.PARTY_QUEST.ACTIVE.isTrue())
            .and(PartyQuest.PARTY_QUEST.QUEST_ID.eq(questId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        if(q.getResultList().isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND));

        //Kontrola či userId už nemá aktívny questId v partyId
        Table<?> stages =
            create.select(Stage.STAGE.ID.as("stage_id"))
            .from(Stage.STAGE)
            .where(Stage.STAGE.QUEST_ID.eq(questId))
            .asTable("stage");

        Select<?> query2 =
            create.select(UserPartyQuest.USER_PARTY_QUEST.ID)
            .from(UserPartyQuest.USER_PARTY_QUEST, stages)
            .where(UserPartyQuest.USER_PARTY_QUEST.USER_ID.eq(userId))
            .and(UserPartyQuest.USER_PARTY_QUEST.STAGE_ID.in(stages.field("stage_id")))
            .and(UserPartyQuest.USER_PARTY_QUEST.STATUS.notEqual(UserQuestStatus.CANCELED.name()))
            .orderBy(UserPartyQuest.USER_PARTY_QUEST.ID.desc())
            .limit(1);

        Query q2 = entityManager.createNativeQuery(query2.getSQL());
        GeolyAPI.setBindParameterValues(q2, query2);
        if(!q2.getResultList().isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_HAS_ACTIVE_QUEST, HttpStatus.METHOD_NOT_ALLOWED));


        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.Quest> quest = questRepository.findByIdAndDaily(questId, false);
        if(!quest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<List<com.geoly.app.models.Stage>> stage = stageRepository.findAllByQuest(quest.get());
        if(!stage.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.STAGE_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.Party> party = partyRepository.findById(partyId);
        if(!party.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.PartyQuest> partyQuest = partyQuestRepository.findByPartyAndQuest(party.get(), quest.get());
        if(!partyQuest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST));

        com.geoly.app.models.UserPartyQuest userPartyQuest = new com.geoly.app.models.UserPartyQuest();
        userPartyQuest.setPartyQuest(partyQuest.get());
        userPartyQuest.setStage(stage.get().get(0));
        userPartyQuest.setUser(user.get());
        userPartyQuest.setStatus(UserQuestStatus.ON_STAGE);
        entityManager.persist(userPartyQuest);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_SIGNED_UP_ON_QUEST, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List signOutPartyQuest(int partyId, int questId, int userId){
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
        GeolyAPI.setBindParameterValues(q, query);
        Object stageId = q.getSingleResult();

        Optional<com.geoly.app.models.UserPartyQuest> userQuest = userPartyQuestRepository.findById(Integer.parseInt(String.valueOf(stageId)));
        if(!userQuest.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_QUEST_NOT_FOUND, HttpStatus.NOT_FOUND));

        userQuest.get().setStatus(UserQuestStatus.CANCELED);
        entityManager.merge(userQuest.get());
        return Collections.singletonList(new ResponseEntity<>(StatusMessage.SIGNED_OUT_OF_QUEST, HttpStatus.OK));
    }

    public List getPartyQuestDetails(int partyId, int questId, int userId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.Party> party = partyRepository.findById(partyId);
        if(!party.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Optional<com.geoly.app.models.PartyUser> partyUser = partyUserRepository.findByUserAndParty(user.get(), party.get());
        if(!partyUser.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.GROUP_NOT_FOUND, HttpStatus.BAD_REQUEST));

        Select<?> query =
            create.select(Stage.STAGE.TYPE, UserPartyQuest.USER_PARTY_QUEST.STATUS, UserPartyQuest.USER_PARTY_QUEST.CREATED_AT, UserPartyQuest.USER_PARTY_QUEST.UPDATED_AT, com.geoly.app.jooq.tables.User.USER.NICK_NAME, com.geoly.app.jooq.tables.User.USER.PROFILE_IMAGE_URL)
            .from(UserPartyQuest.USER_PARTY_QUEST)
            .leftJoin(Stage.STAGE)
                .on(Stage.STAGE.ID.eq(UserPartyQuest.USER_PARTY_QUEST.STAGE_ID))
            .leftJoin(PartyQuest.PARTY_QUEST)
                .on(PartyQuest.PARTY_QUEST.ID.eq(UserPartyQuest.USER_PARTY_QUEST.PARTY_QUEST_ID))
            .leftJoin(com.geoly.app.jooq.tables.User.USER)
                .on(com.geoly.app.jooq.tables.User.USER.ID.eq(UserPartyQuest.USER_PARTY_QUEST.USER_ID))
            .where(Stage.STAGE.QUEST_ID.eq(questId))
            .and(PartyQuest.PARTY_QUEST.PARTY_ID.eq(partyId))
            .and(PartyQuest.PARTY_QUEST.QUEST_ID.eq(questId))
            .orderBy(UserPartyQuest.USER_PARTY_QUEST.USER_ID, UserPartyQuest.USER_PARTY_QUEST.STAGE_ID);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_PLAY_QUEST, HttpStatus.NO_CONTENT));
        return result;
    }
}
