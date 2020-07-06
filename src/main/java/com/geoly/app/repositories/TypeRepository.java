package com.geoly.app.repositories;

import com.geoly.app.models.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Stage, Integer> {
}