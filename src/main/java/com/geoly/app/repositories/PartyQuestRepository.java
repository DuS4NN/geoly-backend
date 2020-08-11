package com.geoly.app.repositories;

import com.geoly.app.models.Party;
import com.geoly.app.models.PartyQuest;
import com.geoly.app.models.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyQuestRepository extends JpaRepository<PartyQuest, Integer> {

    Optional<PartyQuest> findByPartyAndQuest(Party party, Quest quest);
}
