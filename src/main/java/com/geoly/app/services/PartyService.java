package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.repositories.PartyRepository;
import com.geoly.app.repositories.PartyUserRepository;
import com.geoly.app.repositories.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Select;
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

    public PartyService(EntityManager entityManager, DSLContext create, UserRepository userRepository, PartyRepository partyRepository, PartyUserRepository partyUserRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
        this.partyUserRepository = partyUserRepository;
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

}
