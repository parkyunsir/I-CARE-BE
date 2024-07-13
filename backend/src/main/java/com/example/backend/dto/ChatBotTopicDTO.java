package com.example.backend.dto;

import com.example.backend.model.ChatBotTopicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatBotTopicDTO {
    private Long chatBotTopicId;
    private String topic;

    public ChatBotTopicDTO(ChatBotTopicEntity entity) {
        this.chatBotTopicId = entity.getChatBotTopicId();
        this.topic = entity.getTopic();
    }
}
