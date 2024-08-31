package com.example.backend.service;

import com.example.backend.dto.DiaryMakerDTO;
import com.example.backend.model.IconEntity;
import com.example.backend.repository.IconRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IconService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IconRepository iconRepository;

    public List<String> create(String content) {
        try {
            DiaryMakerDTO dto = new DiaryMakerDTO(content);
            String url = "http://127.0.0.1:5000/icon";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String param = objectMapper.writeValueAsString(dto);
            HttpEntity<String> httpEntity = new HttpEntity<>(param, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode iconsNode = root.path("icons");
            List<String> iconsList = new ArrayList<>();
            if (iconsNode.isArray()) {
                for (JsonNode node : iconsNode) {
                    iconsList.add(node.asText());
                }
            }
            return iconsList;
        } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
/*
    public IconEntity show(Long iconId) {
        return iconRepository.findByIconId(iconId);
    }*/
}
