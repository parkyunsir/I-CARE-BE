package com.example.backend.controller;

import com.example.backend.dto.QuestionDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.QuestionEntity;
import com.example.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/question")
// 추후 @AuthenticationPrincipal String parentId 추가 : QuestionController 수정
public class QuestionController {

    @Autowired
    QuestionService questionService;

    // 작성하기
    @PostMapping
    public ResponseEntity<?> answerQuestion(@RequestBody QuestionDTO dto) {
        try {
            String temporaryParentId = "temporary-parentId";
            QuestionEntity entity = QuestionDTO.toEntity(dto);
            entity.setQuestionId(null);
            entity.setParentId(temporaryParentId);
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
    public ResponseEntity<?> showQuestionList() {
        String temporaryParentId = "temporary-parentId";
        String parentId = temporaryParentId;
        List<QuestionEntity> entities = questionService.showList();
        List<QuestionDTO> dtos = entities.stream().map(QuestionDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);

    }
}
