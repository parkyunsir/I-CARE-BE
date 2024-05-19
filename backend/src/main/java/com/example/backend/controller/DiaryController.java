package com.example.backend.controller;

import com.example.backend.dto.DiaryDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.DiaryEntity;
import com.example.backend.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/diary")
public class DiaryController {
    @Autowired
    private DiaryService diaryService;

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
// 할일
// @RequestParam("parentId") String parentId 추가해서 DiaryController, DiaryControllerTest 수정 (@AuthenticationPrincipal 준비)
// IconEntity : iconId, iconImage, keyword
// iconImage 파일 찾기(keyword와 함께)
// showDiaryDetail, showDiaryList 실행할 때 iconImage도 같이 보이도록 하기
// 5월의 일기 리스트(반환 : diaryId, date, iconImage)
// content 분석해서(단어를 뽑아낸다든지) icon 배열[3] 반환
// 다음이 wordCloud : diary 5개 모이면 wordCloud 생성 가능(이미지 파일로)
    @GetMapping
    public ResponseEntity<?> showDiaryDetail(@RequestParam("childId") String childId, @RequestParam("diaryId") String diaryId) {
        String temporaryParentId = "temporary-parentId";
        String parentId = temporaryParentId;
        DiaryEntity entity = diaryService.showDetail(parentId, childId, diaryId);

        DiaryDTO dto = new DiaryDTO(entity);
        return ResponseEntity.ok().body(dto);
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
