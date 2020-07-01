package com.geoly.app.repositories;

import com.geoly.app.models.UserQuestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<UserQuestStatus, Integer> {
}
