package com.example.backend.service;

import com.example.backend.model.QuestionEntity;
import com.example.backend.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;
    
    // 답변하기
    public QuestionEntity answer(QuestionEntity entity) {
        validate(entity);
        if (questionRepository.existsByDate(entity.getDate())) { // 작성한 게 있는지 check
            log.warn("A question for that date already exists.");
            throw new RuntimeException("A question for that date already exists."); // 있다면 오류 발생
        }
        QuestionEntity savedEntity = questionRepository.save(entity); // 없다면 추가하기
        log.info("Entity Id : {} is saved.", savedEntity.getQuestionId());
        return questionRepository.findByQuestionId(savedEntity.getQuestionId());
    }

    // 리스트 보여주기
    public List<QuestionEntity> showList() {
        return questionRepository.findAll();
    }

    // 유효성 검사
    public void validate(QuestionEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if (entity.getParentId() == null) {
            log.warn("Unknown parent");
            throw new RuntimeException("Unknown parent");
        }
        if (entity.getQuestionId() != null) {
            QuestionEntity original = questionRepository.findByQuestionId(entity.getQuestionId());
            if (!original.getParentId().equals(entity.getParentId())) {
                log.warn("Not the owner of the diary");
                throw new RuntimeException("Not the owner of the diary");
            }
        }
    }
}
