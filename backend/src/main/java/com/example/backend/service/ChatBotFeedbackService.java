package com.example.backend.service;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChatBotFeedbackEntity;
import com.example.backend.repository.ChatBotFeedbackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
//챗봇B: 추후 설정 예정
public class ChatBotFeedbackService {
    @Autowired
    ChatBotFeedbackRepository chatBotFeedbackRepository;

    public String feedbackChat(ChatBotFeedbackEntity entity) {
        validate(entity);
        // String feedback = 챗봇 B 호출
        // entity.setFeedback(feedback);
        ChatBotFeedbackEntity savedEntity = chatBotFeedbackRepository.save(entity);
        return savedEntity.getFeedback();
    }

    public List<ChatBotEntity> feedbackList(String childId, String parentId){
        return chatBotFeedbackRepository.findByChildIdAndParentId(childId,parentId);
    }


    public void validate(ChatBotFeedbackEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getChatBotFeedbackId() != null) { // entity, original의 child 인증
        ChatBotFeedbackEntity original = chatBotFeedbackRepository.findByChatBotFeedbackId(entity.getChatBotFeedbackId());
        if(!original.getChildId().equals(entity.getChildId())) {
            log.warn("Not the owner of the diary");
            throw new RuntimeException("Not the owner of the diary");
        }
    }
}


}
