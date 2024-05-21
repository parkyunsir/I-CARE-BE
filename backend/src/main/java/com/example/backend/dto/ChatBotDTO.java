package com.example.backend.dto;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.DiaryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatBotDTO {
    private Long chatId; //대화id
    private LocalDate date; //날짜
    private LocalDate inputTime; //입력 시간
    private String feedback; //피드백

    public ChatBotDTO(ChatBotEntity entity) {
        this.chatId = entity.getChatId();
        this.date = entity.getDate();
        this.inputTime = entity.getInputTime();
        this.feedback = entity.getFeedback();
    }

    public static ChatBotEntity toEntity(final ChatBotDTO dto) {
        return ChatBotEntity.builder()
                .chatId(dto.getChatId())
                .date(dto.getDate())
                .inputTime(dto.getInputTime())
                .feedback(dto.getFeedback())
                .build();
    }
}