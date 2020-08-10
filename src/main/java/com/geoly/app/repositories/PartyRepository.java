package com.geoly.app.repositories;

import com.geoly.app.models.Party;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {

    Optional<Party> findByIdAndUser(int id, User user);

    int deleteByIdAndUser(int id, User user);
}
