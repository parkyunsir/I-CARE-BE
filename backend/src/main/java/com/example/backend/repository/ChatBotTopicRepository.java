package com.example.backend.repository;

import com.example.backend.model.ChatBotTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBotTopicRepository extends JpaRepository<ChatBotTopicEntity, Long> {
    ChatBotTopicEntity findByChatBotTopicId(Long chatBotTopicId);
}
