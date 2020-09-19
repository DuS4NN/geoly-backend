package com.geoly.app.repositories;

import com.geoly.app.models.Party;
import com.geoly.app.models.PartyUser;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyUserRepository extends JpaRepository<PartyUser, Integer> {

    void deleteByPartyAndUser(Party party, User user);

    Optional<PartyUser> findByUserAndParty(User user, Party party);

    int countAllByParty(Party party);
}
