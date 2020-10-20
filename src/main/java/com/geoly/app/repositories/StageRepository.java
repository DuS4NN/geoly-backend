package com.geoly.app.repositories;

import com.geoly.app.models.Quest;
import com.geoly.app.models.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {

    Optional<List<Stage>> findAllByQuest(Quest quest);
    Optional<Stage> findByQuest(Quest quest);
    Optional<Stage> findByQuestAndId(Quest quest, int id);
}
