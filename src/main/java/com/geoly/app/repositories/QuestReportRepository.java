package com.geoly.app.repositories;

import com.geoly.app.models.Quest;
import com.geoly.app.models.QuestReport;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestReportRepository extends JpaRepository<QuestReport, Integer> {

    Optional<QuestReport> findAllByQuestAndUser(Quest quest, User user);
}
