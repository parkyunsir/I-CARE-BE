package com.example.backend.repository;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.DiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotRepository extends JpaRepository<ChatBotEntity, String> {

}
