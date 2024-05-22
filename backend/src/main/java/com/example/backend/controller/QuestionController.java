package com.example.backend.controller;

import com.example.backend.dto.QuestionDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.QuestionEntity;
import com.example.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/question")

public class QuestionController {

    @Autowired
    QuestionService questionService;

    // 작성하기
    @PostMapping //@AuthenticationPrincipal String userId
    public ResponseEntity<?> answerQuestion(@AuthenticationPrincipal String parentId, @RequestBody QuestionDTO dto) {
        try {
            QuestionEntity entity = QuestionDTO.toEntity(dto);
            entity.setQuestionId(null);
            entity.setParentId(parentId); //부모 아이디 설정
            entity.setDate(LocalDate.now()); // 지금 시간
            QuestionEntity savedEntity = questionService.answer(entity);

            QuestionDTO savedDto = new QuestionDTO(savedEntity);
            return ResponseEntity.ok().body(savedDto);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<QuestionDTO> response = ResponseDTO.<QuestionDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 리스트 보여주기
    @GetMapping("/list")
    public ResponseEntity<?> showQuestionList(@AuthenticationPrincipal String parentId) {
        List<QuestionEntity> entities = questionService.showList(parentId);

        List<QuestionDTO> dtos = entities.stream().map(QuestionDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);

    }
    
    // 검색하기 - output(답변)
    @GetMapping // api/question?output=
    public ResponseEntity<?> searchQuestionList(@AuthenticationPrincipal String parentId, @RequestParam String output) {
        List<QuestionEntity> entities = questionService.searchLIst(output);

        List<QuestionDTO> dtos = entities.stream().map(QuestionDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

}