package com.geoly.app.repositories;

import com.geoly.app.models.PartyQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyQuestRepository extends JpaRepository<PartyQuest, Integer> {
}
