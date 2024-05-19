package com.example.backend.repository;

import com.example.backend.model.DiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, String> {
    DiaryEntity findByDiaryId(String diaryId);

    boolean existsByDate(LocalDate date);
}
