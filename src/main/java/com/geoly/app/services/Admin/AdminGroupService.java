package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.Log;
import com.geoly.app.models.LogType;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.repositories.*;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminGroupService {

    private EntityManager entityManager;
    private DSLContext create;
    private UserRepository userRepository;
    private PartyRepository partyRepository;
    private PartyUserRepository partyUserRepository;
    private QuestRepository questRepository;
    private PartyQuestRepository partyQuestRepository;

    public AdminGroupService(EntityManager entityManager, DSLContext create, UserRepository userRepository, PartyRepository partyRepository, PartyUserRepository partyUserRepository, QuestRepository questRepository, PartyQuestRepository partyQuestRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
        this.partyUserRepository = partyUserRepository;
        this.questRepository = questRepository;
        this.partyQuestRepository = partyQuestRepository;
    }

    public Response getGroupDetails(int id){
        Select<?> details =
            create.select(Party.PARTY.NAME, Party.PARTY.CREATED_AT, Party.PARTY.USER_ID, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL)
            .from(Party.PARTY)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Party.PARTY.USER_ID))
            .where(Party.PARTY.ID.eq(id));

        Query q1 = entityManager.createNativeQuery(details.getSQL());
        API.setBindParameterValues(q1, details);
        List detailsList = q1.getResultList();

        Select<?> users =
            create.select(PartyUser.PARTY_USER.CREATED_AT, User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, User.USER.ID)
                .from(PartyUser.PARTY_USER)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(PartyUser.PARTY_USER.USER_ID))
                .where(PartyUser.PARTY_USER.PARTY_ID.eq(id));

        Query q2 = entityManager.createNativeQuery(users.getSQL());
        API.setBindParameterValues(q2, users);
        List usersList = q2.getResultList();

        Select<?> quests =
            create.select(Quest.QUEST.ID, Quest.QUEST.NAME, Quest.QUEST.PRIVATE_QUEST)
                .from(PartyQuest.PARTY_QUEST)
                .leftJoin(Quest.QUEST)
                    .on(Quest.QUEST.ID.eq(PartyQuest.PARTY_QUEST.QUEST_ID))
                .where(PartyQuest.PARTY_QUEST.PARTY_ID.eq(id));

        Query q3 = entityManager.createNativeQuery(quests.getSQL());
        API.setBindParameterValues(q3, quests);
        List questsList = q3.getResultList();

        List<List> result = new ArrayList<>();
        result.add(detailsList);
        result.add(usersList);
        result.add(questsList);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response kickUserFromGroup(int groupId, int userId, int adminId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findById(groupId);
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        if(party.get().getUser().getId() == userId){
            return new Response(StatusMessage.CAN_NOT_KICK_OWNER, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        Optional<com.geoly.app.models.PartyUser> partyUser = partyUserRepository.findByUserAndParty(user.get(), party.get());
        if(!partyUser.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);


        Log log = new Log();
        log.setLogType(LogType.KICK_USER_FROM_GROUP);

        JSONObject jo = new JSONObject();
        jo.put("adminId", adminId);
        jo.put("userId", userId);
        jo.put("groupId", groupId);

        entityManager.persist(log);
        entityManager.remove(partyUser.get());

        return new Response(StatusMessage.USER_KICKED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response deleteQuestFromGroup(int groupId, int questId, int adminId){
        Optional<com.geoly.app.models.Quest> quest = questRepository.findById(questId);
        if(!quest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.Party> party = partyRepository.findById(groupId);
        if(!party.isPresent()) return new Response(StatusMessage.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.PartyQuest> partyQuest = partyQuestRepository.findByPartyAndQuest(party.get(), quest.get());
        if(!partyQuest.isPresent()) return new Response(StatusMessage.QUEST_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Log log = new Log();
        log.setLogType(LogType.REMOVE_QUEST_FROM_GROUP);

        JSONObject jo = new JSONObject();
        jo.put("adminId", adminId);
        jo.put("questId", questId);
        jo.put("groupId", groupId);

        log.setData(jo.toString());

        entityManager.persist(log);
        entityManager.remove(partyQuest.get());

        return new Response(StatusMessage.QUEST_DELETED, HttpStatus.ACCEPTED, null);
    }
}
