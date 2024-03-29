package com.geoly.app.repositories;

import com.geoly.app.models.UserQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<UserQuest, Integer> {
}
