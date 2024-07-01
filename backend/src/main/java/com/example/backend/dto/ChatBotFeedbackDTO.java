package com.example.backend.dto;

import com.example.backend.model.ChatBotFeedbackEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatBotFeedbackDTO {
    private String chatBotFeedbackId;
    private String parentId;
    private String childId;
    private LocalDateTime date; //날짜
    private String feedback; //피드백
    private String parentRequest;

    public ChatBotFeedbackDTO(ChatBotFeedbackEntity entity) {
        this.chatBotFeedbackId = entity.getChatBotFeedbackId();
        this.parentId = entity.getParentId();
        this.childId = entity.getChildId();
        this.date = entity.getDate();
        this.feedback = entity.getFeedback();
        this.parentRequest = entity.getParentRequest();
    }

    public static ChatBotFeedbackEntity toEntity(final ChatBotFeedbackDTO dto) {
        return ChatBotFeedbackEntity.builder()
                .chatBotFeedbackId(dto.getChatBotFeedbackId())
                .parentId(dto.getParentId())
                .childId(dto.getChildId())
                .date(dto.getDate())
                .feedback(dto.getFeedback())
                .parentRequest(dto.getParentRequest())
                .build();
    }
}