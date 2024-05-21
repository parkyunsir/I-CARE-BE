package com.example.backend.repository;

import com.example.backend.model.ChatBotFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotFeedbackRepository extends JpaRepository<ChatBotFeedbackEntity, String> {

}
