package com.geoly.app.repositories;

import com.geoly.app.models.PartyInvate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyInvateRepository extends JpaRepository<PartyInvate, Integer> {
}

