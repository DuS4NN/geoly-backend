package com.geoly.app.repositories;

import com.geoly.app.models.User;
import com.geoly.app.models.UserOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOptionRepository extends JpaRepository<UserOption, Integer> {

    Optional<UserOption> findByUser(User user);
}
