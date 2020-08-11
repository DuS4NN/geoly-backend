package com.geoly.app.repositories;

import com.geoly.app.models.UserPartyQuest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPartyQuestRepository extends JpaRepository<UserPartyQuest, Integer> {
}
