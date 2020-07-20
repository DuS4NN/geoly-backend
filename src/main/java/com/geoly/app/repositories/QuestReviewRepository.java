package com.geoly.app.repositories;

import com.geoly.app.models.Quest;
import com.geoly.app.models.QuestReview;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestReviewRepository extends JpaRepository<QuestReview, Integer> {

    Optional<QuestReview> findByIdAndUserAndQuest(int id, User user, Quest quest);
}
