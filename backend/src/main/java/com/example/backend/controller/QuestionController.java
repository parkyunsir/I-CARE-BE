package com.example.backend.controller;

import com.example.backend.dto.QuestionDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.InputEntity;
import com.example.backend.model.QuestionEntity;
import com.example.backend.repository.InputRepository;
import com.example.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/question")

public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    InputRepository inputRepository;

    // 작성하기
    @PostMapping //@AuthenticationPrincipal String userId
    public ResponseEntity<?> addQuestion(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestBody QuestionDTO dto) {
        try {
            QuestionEntity entity = QuestionDTO.toEntity(dto);
            entity.setQuestionId(null);
            entity.setParentId(parentId); //부모 아이디 설정
            entity.setChildId(childId);
            entity.setDate(LocalDate.now()); // 오늘 날짜
//            entity.setDate(LocalTime.now().withSecond(0).withNano(0)); // 지금 시간

//            int secondOfDay  = LocalTime.now().getMinute();
//            long inputId = secondOfDay % 11 + 1;
//            entity.setInputId(inputId);

            int date  = LocalDate.now().getDayOfYear();
            long inputId =  date % 11 + 1;
            entity.setInputId(inputId);


            InputEntity inputEntity = inputRepository.findByInputId(inputId);
            entity.setInput(inputEntity.getInput());


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
    @GetMapping
    public ResponseEntity<?> showQuestionList(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        List<QuestionEntity> entities = questionService.showList(parentId);

        List<QuestionDTO> dtos = entities.stream().map(QuestionDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);

    }
    
    // 검색하기 - output(답변)
    @GetMapping("/search") // api/question/search?childId={}&output={}
    public ResponseEntity<?> searchQuestionList(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestParam("output") String output) {
        List<QuestionEntity> entities = questionService.searchLIst(output);

        List<QuestionDTO> dtos = entities.stream().map(QuestionDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }
}