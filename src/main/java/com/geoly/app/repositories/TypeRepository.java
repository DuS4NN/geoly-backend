package com.geoly.app.repositories;

import com.geoly.app.models.StageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<StageType, Integer> {
}
