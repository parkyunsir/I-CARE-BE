package com.example.backend.service;

import com.example.backend.model.QuestionEntity;
import com.example.backend.repository.ChildRepository;
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
    @Autowired
    private ChildRepository childRepository;
    // 답변하기
    public QuestionEntity answer(QuestionEntity entity) {
        validate(entity);
        if (questionRepository.existsByChildIdAndDate(entity.getChildId(), entity.getDate()) != null){
            log.warn("A question for that date already exists.");
            throw new RuntimeException("A question for that date already exists.");
        }
        QuestionEntity savedEntity = questionRepository.save(entity); // 없다면 추가하기
        log.info("Entity Id : {} is saved.", savedEntity.getQuestionId());
        return questionRepository.findByQuestionId(savedEntity.getQuestionId());
    }

    // 리스트 보여주기
    public List<QuestionEntity> showList(final String parentId) {
        //일치하는 부모Id만 보여주기
        return questionRepository.findByParentId(parentId);
    }

    // 검색하기
    public List<QuestionEntity> searchLIst(final String output) {
        return questionRepository.findAllByOutputContaining(output);
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
        if(!entity.getParentId().equals(childRepository.findByChildId(entity.getChildId()).getParentId())) { // entity의 parent, child 인증
            log.warn("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }
        if (entity.getQuestionId() != null) {
            QuestionEntity original = questionRepository.findByQuestionId(entity.getQuestionId());
            if (!original.getParentId().equals(entity.getParentId())) {
                log.warn("Not the owner of the question");
                throw new RuntimeException("Not the owner of the question");
            }
        }
    }
}
