package com.example.backend.service;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChildEntity;
import com.example.backend.repository.ChatBotRepository;
import com.example.backend.repository.ChildRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatBotService {
    @Autowired
    private ChatBotRepository chatBotRepository;

    @Autowired
    private ChildRepository childRepository;

    @Value("${openai.api.key.a}")
    private String apiKey;

    @Value("${openai.model.id.a}")
    private String modelId;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ChatBotEntity> createResponse(ChatBotEntity entity) throws IOException {
        validate(entity);
        String response = getCompletion(entity.getRequest());
        entity.setResponse(response);
        chatBotRepository.save(entity);
        return chatBotRepository.findByChildId(entity.getChildId());
    }

    public String getCompletion(String prompt) throws IOException {
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        JSONObject json = new JSONObject();
        json.put("model", modelId);
        json.put("messages", new JSONObject[] {message});

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful() || response.body() == null) {
                throw new IOException("Unexpected : " + response);
            }
            String responseBody =  response.body().string(); //print(completion.choices[0].message)
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            return jsonNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
        }
    }

    @Transactional
    public List<ChatBotEntity> delete(String parentId, String childId) {
        ChatBotEntity entity = ChatBotEntity.builder()
                        .parentId(parentId)
                        .childId(childId)
                        .build();
        validate(entity);
        chatBotRepository.deleteByParentIdAndChildId(parentId, childId);
        return chatBotRepository.findByChildId(childId);
    }

    public void validate(ChatBotEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if (entity.getParentId() == null || entity.getChildId() == null) {
            log.warn("Unknown parent or child.");
            throw new RuntimeException("Unknown parent or child.");
        }
        Optional<ChildEntity> childEntityOpt = childRepository.findByChildId(entity.getChildId());
        if (childEntityOpt.isEmpty() || !entity.getParentId().equals(childEntityOpt.get().getParentId())) {
            log.warn("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }
    }
}
