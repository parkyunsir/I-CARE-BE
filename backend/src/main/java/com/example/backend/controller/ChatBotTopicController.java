package com.example.backend.controller;

import com.example.backend.dto.ChatBotTopicDTO;
import com.example.backend.dto.DiaryDTO;
import com.example.backend.model.ChatBotTopicEntity;
import com.example.backend.model.DiaryEntity;
import com.example.backend.service.ChatBotTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/chatbot/topic")
public class ChatBotTopicController {
    @Autowired
    private ChatBotTopicService chatBotTopicService;

    @GetMapping
    public ResponseEntity<?> showTopicList() {
        List<ChatBotTopicEntity> entities = chatBotTopicService.showList();
        List<ChatBotTopicDTO> dtos = entities.stream().map(ChatBotTopicDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/one")
    public ResponseEntity<?> showTopicOne(@RequestParam("chatBotTopicId") Long chatBotTopicId) {
        ChatBotTopicEntity entity = chatBotTopicService.showOne(chatBotTopicId);
        ChatBotTopicDTO dto = new ChatBotTopicDTO(entity);
        return ResponseEntity.ok().body(dto);
    }
}
