package com.example.backend.controller;

import com.example.backend.service.ChatBotFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/chatbot")
public class ChatBotFeedbackController {
    @Autowired
    private ChatBotFeedbackService service;
}
