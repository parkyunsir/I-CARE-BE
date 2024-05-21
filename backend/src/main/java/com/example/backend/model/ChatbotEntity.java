package com.example.backend.model;

import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Table()
public class ChatbotEntity {
    private LocalDateTime date; //날짜
    private LocalDateTime input_time; //입력 시간
    private String feedback; //피드백
}
