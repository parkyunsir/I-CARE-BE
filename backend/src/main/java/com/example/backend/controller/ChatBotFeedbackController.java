package com.example.backend.controller;

import com.example.backend.dto.ChatBotDTO;
import com.example.backend.dto.ChatBotFeedbackDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChatBotFeedbackEntity;
import com.example.backend.service.ChatBotFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/chatbot/feedback")
public class ChatBotFeedbackController {
    @Autowired
    private ChatBotFeedbackService chatBotFeedbackService;

    @PostMapping //챗봇(대화 목록)에서 feed back 출력..
    public ResponseEntity<?> showFeedback(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        try {

            ChatBotFeedbackEntity feedback = chatBotFeedbackService.feedbackChat(parentId,childId);

            return ResponseEntity.ok().body(feedback);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<ChatBotFeedbackDTO> response = ResponseDTO.<ChatBotFeedbackDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping("/list") //feed back list
    public ResponseEntity<?> listFeedback(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        List<ChatBotFeedbackEntity> entities = chatBotFeedbackService.feedbackList(childId, parentId);
        List<ChatBotFeedbackDTO> dtos = entities.stream().map(ChatBotFeedbackDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

}
