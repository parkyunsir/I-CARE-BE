package com.example.backend.controller;

import com.example.backend.dto.InputDTO;
import com.example.backend.model.InputEntity;
import com.example.backend.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/question/input")
public class InputController {

    @Autowired
    public InputService inputService;

    @GetMapping
    public ResponseEntity<?> showInputDetail(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestParam("inputId") Long inputId) {
        InputEntity entity = inputService.showInput(inputId);
        InputDTO dto = new InputDTO(entity);
        return ResponseEntity.ok().body(dto);
    }
}