package com.geoly.app.repositories;

import com.geoly.app.models.Party;
import com.geoly.app.models.PartyInvite;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyInviteRepository extends JpaRepository<PartyInvite, Integer> {

    Optional<PartyInvite> findByUserAndParty(User user, Party party);
}

