package com.geoly.app.repositories;

import com.geoly.app.models.User;
import com.geoly.app.models.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Integer> {

    Optional<UserReport> findAllByUserComplainantAndUserReported(User complainant, User reported);
}
