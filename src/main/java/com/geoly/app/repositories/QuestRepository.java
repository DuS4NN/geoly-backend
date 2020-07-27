package com.geoly.app.repositories;

import com.geoly.app.models.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Integer> {

    Optional<Quest> findByDaily(boolean daily);

    Optional<Quest> findByIdAndDaily(int id, boolean daily);
}
