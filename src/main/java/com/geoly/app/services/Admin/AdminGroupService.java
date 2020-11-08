package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.*;
import com.geoly.app.models.StatusMessage;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminGroupService {

    private EntityManager entityManager;
    private DSLContext create;

    public AdminGroupService(EntityManager entityManager, DSLContext create) {
        this.entityManager = entityManager;
        this.create = create;
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
}
