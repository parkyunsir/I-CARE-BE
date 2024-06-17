package com.example.backend.repository;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChatBotFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChatBotFeedbackRepository extends JpaRepository<ChatBotFeedbackEntity, String> {
    //부모와 자식 id 모두 일치
    ChatBotFeedbackEntity findByChatBotFeedbackId(String chatBotFeedbackId);

    List<ChatBotFeedbackEntity> findByChildId(String childId);
}
