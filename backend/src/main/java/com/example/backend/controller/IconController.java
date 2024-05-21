package com.example.backend.controller;

import com.example.backend.dto.IconDTO;
import com.example.backend.model.IconEntity;
import com.example.backend.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/diary/icon")
public class IconController {
    @Autowired
    private IconService iconService;
/*
    @GetMapping
    public ResponseEntity<?> offerIconList(@RequestParam("content") String content) {
        List<IconEntity> entities = iconService.offerList(content);
        List<IconDTO> dtos = entities.stream().map(IconDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }*/

    @GetMapping
    public ResponseEntity<?> showIcon(@RequestParam("iconId") Long iconId) {
        IconEntity entity = iconService.show(iconId);
        IconDTO dto = new IconDTO(entity);
        return ResponseEntity.ok().body(dto);
    }
}
