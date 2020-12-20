package com.geoly.app.repositories;

import com.geoly.app.models.Stage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserPartyQuest;
import com.geoly.app.models.UserQuestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPartyQuestRepository extends JpaRepository<UserPartyQuest, Integer> {

    Optional<UserPartyQuest> findByUserAndStageAndStatus(User user, Stage stage, UserQuestStatus status);
}