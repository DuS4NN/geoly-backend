package com.geoly.app.repositories;

import com.geoly.app.models.QuestReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestReviewRepository extends JpaRepository<QuestReview, Integer> {
}
