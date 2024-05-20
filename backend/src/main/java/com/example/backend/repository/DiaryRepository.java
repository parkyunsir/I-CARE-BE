package com.example.backend.repository;

import com.example.backend.model.DiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, String> {
    DiaryEntity findByDiaryId(String diaryId);

    boolean existsByDate(LocalDate date);

    @Query(value = "SELECT * FROM Diary WHERE MONTH(date) = :month", nativeQuery = true)
    List<DiaryEntity> findByDateMonth(@Param("month") Long month);
}
