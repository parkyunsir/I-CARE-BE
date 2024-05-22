package com.example.backend.repository;

import com.example.backend.model.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {
    List<QuestionEntity> findByParentId(String parentId); // 내가 작성한 거 전체 출력
    QuestionEntity findByQuestionId(String questionId);
    boolean existsByDate(LocalDate date); //작성한 답변이 있는지 확인

    //내용 검색
    List<QuestionEntity> findAllByOutputContaining(String output);
}
