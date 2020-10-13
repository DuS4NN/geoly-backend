package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Party;
import com.geoly.app.jooq.tables.PartyInvite;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.models.PartyInvateStatus;
import com.geoly.app.models.PartyUser;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.repositories.PartyInviteRepository;
import com.geoly.app.repositories.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;

@Service
public class InviteService {

    private DSLContext create;
    private EntityManager entityManager;
    private PartyInviteRepository partyInviteRepository;
    private UserRepository userRepository;

    public InviteService(DSLContext create, EntityManager entityManager, PartyInviteRepository partyInviteRepository, UserRepository userRepository) {
        this.create = create;
        this.entityManager = entityManager;
        this.partyInviteRepository = partyInviteRepository;
        this.userRepository = userRepository;
    }

    public Response getPendingInvites(int userId, int count){
        Select<?> query =
            create.select(PartyInvite.PARTY_INVITE.ID, User.USER.NICK_NAME, Party.PARTY.NAME, Party.PARTY.ID.as("partyId"))
            .from(PartyInvite.PARTY_INVITE)
            .leftJoin(Party.PARTY)
                .on(Party.PARTY.ID.eq(PartyInvite.PARTY_INVITE.PARTY_ID))
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Party.PARTY.USER_ID))
            .where(PartyInvite.PARTY_INVITE.USER_ID.eq(userId))
            .and(PartyInvite.PARTY_INVITE.STATUS.eq(PartyInvateStatus.PENDING.name()))
            .orderBy(PartyInvite.PARTY_INVITE.CREATED_AT.desc())
            .limit(10)
            .offset(count);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public void setUnseen(int userId){
        create.update(PartyInvite.PARTY_INVITE).set(field("seen"), 1)
            .where(PartyInvite.PARTY_INVITE.SEEN.isFalse())
            .and(PartyInvite.PARTY_INVITE.USER_ID.eq(userId))
            .execute();
    }

    @Transactional(rollbackOn = Exception.class)
    public Response cancelInvite(int inviteId, int userId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.PartyInvite> partyInvite = partyInviteRepository.findByUserAndIdAndStatus(user.get(), inviteId, PartyInvateStatus.PENDING);
        if(!partyInvite.isPresent()) return new Response(StatusMessage.INVITE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        partyInvite.get().setStatus(PartyInvateStatus.DENIED);
        entityManager.merge(partyInvite.get());

        return new Response(StatusMessage.INVITE_DENIED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response acceptInvite(int inviteId, int userId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<com.geoly.app.models.PartyInvite> partyInvite = partyInviteRepository.findByUserAndIdAndStatus(user.get(), inviteId, PartyInvateStatus.PENDING);
        if(!partyInvite.isPresent()) return new Response(StatusMessage.INVITE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        partyInvite.get().setStatus(PartyInvateStatus.ACCEPTED);

        PartyUser partyUser = new PartyUser();
        partyUser.setUser(user.get());
        partyUser.setParty(partyInvite.get().getParty());

        entityManager.merge(partyInvite.get());
        entityManager.persist(partyUser);
        return new Response(StatusMessage.INVITE_ACCEPTED, HttpStatus.ACCEPTED, null);
    }

}
