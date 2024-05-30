package com.example.backend.repository;

import com.example.backend.model.ChatBotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatBotRepository extends JpaRepository<ChatBotEntity, String> {
    List<ChatBotEntity> findByChildId(String childId);
}
