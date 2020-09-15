package com.geoly.app.repositories;

import com.geoly.app.models.Image;
import com.geoly.app.models.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    void deleteAllByQuest(Quest quest);

    Optional<Image> findByIdAndQuest(int id, Quest quest);
}
