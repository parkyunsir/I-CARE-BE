package com.example.backend.service;

import com.example.backend.repository.ChatBotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatBotService {
    @Autowired
    ChatBotRepository repository;

}
