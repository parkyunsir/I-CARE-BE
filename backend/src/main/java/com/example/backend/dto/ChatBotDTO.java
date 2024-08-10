package com.example.backend.dto;

import com.example.backend.model.ChatBotEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatBotDTO {
    private String chatBotId;
    private String request;
    private String response;
    private Long chatBotTopicId;

    public ChatBotDTO(ChatBotEntity entity) {
        this.chatBotId = entity.getChatBotId();
        this.request = entity.getRequest();
        this.response = entity.getResponse();
        this.chatBotTopicId = entity.getChatBotTopicId();
    }

    public static ChatBotEntity toEntity(final ChatBotDTO dto) {
        return ChatBotEntity.builder()
                .chatBotId(dto.getChatBotId())
                .request(dto.getRequest())
                .response(dto.getResponse())
                .chatBotTopicId(dto.getChatBotTopicId())
                .build();
    }
}
