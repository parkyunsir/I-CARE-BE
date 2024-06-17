package com.example.backend.controller;

import com.example.backend.dto.ChatBotDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.ChatBotEntity;
import com.example.backend.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/chatbot")
public class ChatBotController {
    @Autowired
    private ChatBotService chatBotService;

    @PostMapping
    public ResponseEntity<?> createResponse(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestBody ChatBotDTO dto) {
        try {
            ChatBotEntity entity = ChatBotDTO.toEntity(dto);
            entity.setChatBotId(null);
            entity.setParentId(parentId);
            entity.setChildId(childId);
            entity.setResponse(null);
            ChatBotEntity savedEntity = chatBotService.createResponse(entity);

            ChatBotDTO savedDto = new ChatBotDTO(savedEntity);
            return ResponseEntity.ok().body(savedDto);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<ChatBotDTO> response = ResponseDTO.<ChatBotDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteChatBot(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        try {
            List<ChatBotEntity> entities = chatBotService.delete(parentId, childId);
            List<ChatBotDTO> dtos = entities.stream().map(ChatBotDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok().body(dtos);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<ChatBotDTO> response = ResponseDTO.<ChatBotDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}