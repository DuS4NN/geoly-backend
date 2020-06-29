package com.geoly.app.repositories;

import com.geoly.app.models.Log;
import com.geoly.app.models.PartyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyUserRepository extends JpaRepository<PartyUser, Integer> {
}
