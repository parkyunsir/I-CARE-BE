package com.example.backend.service;

import com.example.backend.model.ChatBotTopicEntity;
import com.example.backend.repository.ChatBotTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatBotTopicService {
    @Autowired
    private ChatBotTopicRepository chatBotTopicRepository;

    public List<ChatBotTopicEntity> showList() {
        return chatBotTopicRepository.findAll();
    }

    public ChatBotTopicEntity showOne(Long chatBotTopicId) {
        return chatBotTopicRepository.findByChatBotTopicId(chatBotTopicId);
    }
}
