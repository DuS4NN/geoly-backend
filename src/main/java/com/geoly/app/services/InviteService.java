package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.tables.Party;
import com.geoly.app.jooq.tables.PartyInvite;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.models.PartyInvateStatus;
import com.geoly.app.models.PartyUser;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.repositories.PartyInviteRepository;
import com.geoly.app.repositories.PartyRepository;
import com.geoly.app.repositories.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public List getPendingInvites(int userId){
        Select<?> query =
            create.select(User.USER.ID, User.USER.NICK_NAME, Party.PARTY.NAME, Party.PARTY.ID.as("partyId"))
            .from(PartyInvite.PARTY_INVITE)
            .leftJoin(Party.PARTY)
                .on(Party.PARTY.ID.eq(PartyInvite.PARTY_INVITE.PARTY_ID))
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Party.PARTY.USER_ID))
            .where(PartyInvite.PARTY_INVITE.USER_ID.eq(userId))
            .and(PartyInvite.PARTY_INVITE.STATUS.eq(PartyInvateStatus.PENDING.name()));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.NO_INVITES, HttpStatus.NO_CONTENT));

        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public List cancelInvite(int inviteId, int userId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.PartyInvite> partyInvite = partyInviteRepository.findByIdAndStatus(inviteId, PartyInvateStatus.PENDING);
        if(!partyInvite.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.INVITE_NOT_FOUND, HttpStatus.NOT_FOUND));

        partyInvite.get().setStatus(PartyInvateStatus.DENIED);
        entityManager.merge(partyInvite.get());
        return Collections.singletonList(new ResponseEntity<>(StatusMessage.INVITE_DENIED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List acceptInvite(int inviteId, int userId){
        Optional<com.geoly.app.models.User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<com.geoly.app.models.PartyInvite> partyInvite = partyInviteRepository.findByIdAndStatus(inviteId, PartyInvateStatus.PENDING);
        if(!partyInvite.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.INVITE_NOT_FOUND, HttpStatus.NOT_FOUND));

        partyInvite.get().setStatus(PartyInvateStatus.ACCEPTED);

        PartyUser partyUser = new PartyUser();
        partyUser.setUser(user.get());
        partyUser.setParty(partyInvite.get().getParty());

        entityManager.merge(partyInvite.get());
        entityManager.persist(partyUser);
        return Collections.singletonList(new ResponseEntity<>(StatusMessage.INVITE_ACCEPT, HttpStatus.OK));
    }

}
