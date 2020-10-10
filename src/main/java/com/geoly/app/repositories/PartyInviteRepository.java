package com.geoly.app.repositories;

import com.geoly.app.models.Party;
import com.geoly.app.models.PartyInvateStatus;
import com.geoly.app.models.PartyInvite;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyInviteRepository extends JpaRepository<PartyInvite, Integer> {

    Optional<PartyInvite> findByUserAndPartyAndStatus(User user, Party party, PartyInvateStatus partyInvateStatus);

    Optional<PartyInvite> findByIdAndStatus(int id, PartyInvateStatus partyInvateStatus);

    Optional<PartyInvite> findByUserAndIdAndStatus(User user, int id, PartyInvateStatus partyInvateStatus);
}

