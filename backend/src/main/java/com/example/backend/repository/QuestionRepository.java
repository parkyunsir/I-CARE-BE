package com.example.backend.repository;

import com.example.backend.model.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {
    QuestionEntity findByQuestionId(String QuestionId);
    boolean existsByDate(LocalDate date); //작성한 답변이 있는지 확인
}
