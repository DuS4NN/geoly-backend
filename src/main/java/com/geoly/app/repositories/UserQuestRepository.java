package com.geoly.app.repositories;

import com.geoly.app.models.Stage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserQuest;
import com.geoly.app.models.UserQuestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestRepository extends JpaRepository<UserQuest, Integer> {

    Optional<List<UserQuest>> findAllByStageAndStatus(Stage stage, UserQuestStatus status);
    Optional<UserQuest> findByUserAndStageAndStatus(User user, Stage stage, UserQuestStatus status);
}
