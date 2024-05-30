package com.example.backend.controller;

import com.example.backend.dto.DiaryDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.DiaryEntity;
import com.example.backend.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/diary")
public class DiaryController {
    @Autowired
    private DiaryService diaryService;

    @PostMapping
    public ResponseEntity<?> createDiary(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestBody DiaryDTO dto) {
        try {
            //String parentId = "temporary-parentId";
            DiaryEntity entity = DiaryDTO.toEntity(dto);
            entity.setDiaryId(null);
            entity.setParentId(parentId);
            entity.setChildId(childId);
            //entity.setDate(LocalDate.now()); // 오늘 날짜 설정
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
    public ResponseEntity<?> showDiaryDetail(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestParam("diaryId") String diaryId) {
        //String parentId = "temporary-parentId";
        DiaryEntity entity = diaryService.showDetail(parentId, childId, diaryId);

        DiaryDTO dto = new DiaryDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> showDiaryDateList(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestParam("start") LocalDate startDate, @RequestParam("end") LocalDate endDate) {
        //String parentId = "temporary-parentId";
        List<DiaryEntity> entities = diaryService.showDateList(parentId, childId, startDate, endDate);

        List<DiaryDTO> dtos = entities.stream().map(DiaryDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @PutMapping
    public ResponseEntity<?> updateDiary(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestBody DiaryDTO dto) {
        //String parentId = "temporary-parentId";
        DiaryEntity entity = DiaryDTO.toEntity(dto);
        entity.setParentId(parentId);
        entity.setChildId(childId);
        DiaryEntity updatedEntity = diaryService.update(entity);

        DiaryDTO updatedDto = new DiaryDTO(updatedEntity);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiary(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestBody DiaryDTO dto) {
        try {
            //String parentId = "temporary-parentId";
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
