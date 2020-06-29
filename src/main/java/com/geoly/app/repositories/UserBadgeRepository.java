package com.geoly.app.repositories;

import com.geoly.app.models.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {
}
