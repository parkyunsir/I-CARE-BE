package com.example.backend.controller;

import com.example.backend.dto.DiaryDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.DiaryEntity;
import com.example.backend.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/diary")
public class DiaryController {
    @Autowired
    private DiaryService diaryService;
    // 추후 @AuthenticationPrincipal String parentId 추가 : DiaryController, DiaryControllerTest 수정
    @PostMapping
    public ResponseEntity<?> createDiary(@RequestParam("childId") String childId, @RequestBody DiaryDTO dto) {
        try {
            String temporaryParentId = "temporary-parentId";
            DiaryEntity entity = DiaryDTO.toEntity(dto);
            entity.setDiaryId(null);
            entity.setParentId(temporaryParentId);
            entity.setChildId(childId);
            entity.setDate(LocalDate.now()); // 오늘 날짜 설정
            DiaryEntity savedEntity = diaryService.create(entity);

            DiaryDTO savedDto = new DiaryDTO(savedEntity);
            return ResponseEntity.ok().body(savedDto);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> showDiaryDetail(@RequestParam("childId") String childId, @RequestParam("diaryId") String diaryId) {
        String temporaryParentId = "temporary-parentId";
        String parentId = temporaryParentId;
        DiaryEntity entity = diaryService.showDetail(parentId, childId, diaryId);

        DiaryDTO dto = new DiaryDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> showDiaryMonthlyList(@RequestParam("childId") String childId, @RequestParam("month") Long month) {
        String temporaryParentId = "temporary-parentId";
        String parentId = temporaryParentId;
        List<DiaryEntity> entities = diaryService.showMonthlyList(parentId, childId, month);

        List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @PutMapping
    public ResponseEntity<?> updateDiary(@RequestParam("childId") String childId, @RequestBody DiaryDTO dto) {
        String temporaryParentId = "temporary-parentId";
        String parentId = temporaryParentId;
        DiaryEntity entity = DiaryDTO.toEntity(dto);
        entity.setParentId(parentId);
        entity.setChildId(childId);
        DiaryEntity updatedEntity = diaryService.update(entity);

        DiaryDTO updatedDto = new DiaryDTO(updatedEntity);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiary(@RequestParam("childId") String childId, @RequestBody DiaryDTO dto) {
        try {
            String temporaryParentId = "temporary-parentId";
            String parentId = temporaryParentId;
            DiaryEntity entity = DiaryDTO.toEntity(dto);
            entity.setParentId(parentId);
            entity.setChildId(childId);
            LocalDate deletedDate = diaryService.delete(entity);

            return ResponseEntity.ok().body(deletedDate);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<DiaryDTO> response = ResponseDTO.<DiaryDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
