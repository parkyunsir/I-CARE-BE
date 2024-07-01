package com.example.backend.repository;

import com.example.backend.model.DiaryEntity;
import com.example.backend.model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, String> {
    DiaryEntity findByDiaryId(String diaryId);

    boolean existsByDate(LocalDate date);

    List<DiaryEntity> findByChildId(String childId);

    List<DiaryEntity> findByChildIdAndDateBetween(String childId, LocalDate startDate, LocalDate endDate);

    DiaryEntity findByChildIdAndDate(String childId, LocalDate date);

    List<DiaryEntity> findByChildIdAndDateAfter(String childId, LocalDate date);
}
