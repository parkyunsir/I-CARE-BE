package com.example.backend.controller;

import com.example.backend.dto.ChatBotDTO;
import com.example.backend.dto.ChatBotFeedbackDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChatBotFeedbackEntity;
import com.example.backend.service.ChatBotFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/chatbot/feedback")
public class ChatBotFeedbackController {
    @Autowired
    private ChatBotFeedbackService chatBotFeedbackService;

    @GetMapping("/list") //feed back list
    public ResponseEntity<?> listFeedback(@RequestParam("childId") String childId, @RequestParam("parentId") String parentId) {
        List<ChatBotEntity> entities = chatBotFeedbackService.feedbackList(childId, parentId);
        List<ChatBotDTO> dtos = entities.stream().map(ChatBotDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }


    @GetMapping //챗봇(대화 목록)에서 feed back 출력..
    public ResponseEntity<?> showFeedback(@RequestBody ChatBotFeedbackDTO dto) {
        try {
            ChatBotFeedbackEntity entity = ChatBotFeedbackDTO.toEntity(dto);

            String feedback = chatBotFeedbackService.feedbackChat(entity);
            entity.setFeedback(feedback);

            ChatBotFeedbackDTO savedDto = new ChatBotFeedbackDTO(entity);
            return ResponseEntity.ok().body(savedDto);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<ChatBotFeedbackDTO> response = ResponseDTO.<ChatBotFeedbackDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

}
