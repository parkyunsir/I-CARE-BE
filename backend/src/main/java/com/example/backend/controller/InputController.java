package com.example.backend.controller;

import com.example.backend.dto.InputDTO;
import com.example.backend.model.InputEntity;
import com.example.backend.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/question/input")
public class InputController {

    @Autowired
    public InputService inputService;

    @GetMapping("/list")
    public ResponseEntity<?> offerInputList(@RequestParam("date") LocalDate date) {
        List<InputEntity> entities = inputService.offerList(date);
        List<InputDTO> dtos = entities.stream().map(InputDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping
    public ResponseEntity<?> showInput(@RequestParam("date") LocalDate date){
        InputEntity entity = inputService.show(date);
        InputDTO dto = new InputDTO(entity);
        return ResponseEntity.ok().body(dto);
    }
}