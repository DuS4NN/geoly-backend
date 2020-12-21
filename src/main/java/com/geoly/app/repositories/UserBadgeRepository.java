package com.geoly.app.repositories;

import com.geoly.app.models.Badge;
import com.geoly.app.models.User;
import com.geoly.app.models.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {

    Optional<UserBadge> findByUserAndBadge(User user, Badge badge);
}
