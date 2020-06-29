package com.geoly.app.repositories;

import com.geoly.app.models.QuestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestReportRepository extends JpaRepository<QuestReport, Integer> {
}
