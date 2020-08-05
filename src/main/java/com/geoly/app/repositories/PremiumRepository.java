package com.geoly.app.repositories;

import com.geoly.app.models.Premium;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremiumRepository extends JpaRepository<Premium, Integer> {

    Optional<Premium> findByUserAndState(User user, String state);
}
