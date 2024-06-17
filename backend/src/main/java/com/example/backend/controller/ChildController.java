package com.example.backend.controller;

import com.example.backend.dto.ChildDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.ChildEntity;
import com.example.backend.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/child")
public class ChildController {

    @Autowired
    private ChildService service;

    @PostMapping
    public ResponseEntity<?> createChild(@AuthenticationPrincipal String parentId, @RequestBody ChildDTO dto) {
        try {
            ChildEntity entity = ChildDTO.toEntity(dto);
            entity.setParentId(parentId);
            entity.setChildId(null);
            List<ChildEntity> entities = service.create(entity); // 서비스 계층에 entity 생성 요청
            List<ChildDTO> dtos = entities.stream().map(ChildDTO::new).collect(Collectors.toList());
            ResponseDTO<ChildDTO> response = ResponseDTO.<ChildDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ChildDTO> response = ResponseDTO.<ChildDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> showChildList(@AuthenticationPrincipal String parentId) {
        List<ChildEntity> entities = service.showList(parentId);
        List<ChildDTO> dtos = entities.stream().map(ChildDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping
    public ResponseEntity<?> showChild(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        ChildEntity entity = service.show(parentId, childId);
        ChildDTO dto = new ChildDTO(entity);
        return ResponseEntity.ok().body(dto);
    }
}
