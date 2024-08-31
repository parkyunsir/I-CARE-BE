package com.example.backend.service;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChildEntity;
import com.example.backend.repository.ChatBotRepository;
import com.example.backend.repository.ChatBotTopicRepository;
import com.example.backend.repository.ChildRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
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

    @Autowired
    private ChatBotTopicRepository chatBotTopicRepository;

    @Value("${openai.api.key.b}")
    private String apiKey;

    @Value("${openai.model.id.a}")
    private String modelId;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatBotEntity createResponse(ChatBotEntity entity) throws IOException {
        validate(entity);
        String topic = chatBotTopicRepository.findByChatBotTopicId(entity.getChatBotTopicId()).getTopic();
        String response = getCompletion(entity.getChildId(), entity.getRequest(), topic);
        entity.setResponse(response);
        return chatBotRepository.save(entity);
    }

    public String getCompletion(String childId, String prompt, String topic) throws IOException {
        List<ChatBotEntity> entities = chatBotRepository.findByChildId(childId);
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("한글로 말해. 자녀는 너야. 너는 장난기 많고 반말을 하는 7살 아이야. '")
                .append(topic)
                .append("'에서 부모인 user와 대화하는 중이야. 앞의 대화 내용은 ");

        // entities 리스트를 순회하면서 request와 response 추가
        for (ChatBotEntity entity : entities) {
            String request = entity.getRequest();
            String response = entity.getResponse();

            contentBuilder.append("부모가 '")
                    .append(request)
                    .append("', 너가 '")
                    .append(response)
                    .append("', ");
        }

        // 마지막 쉼표와 공백 제거
        int length = contentBuilder.length();
        if (length > 2) {
            contentBuilder.setLength(length - 2); // 마지막 두 문자 제거 (', ')
        }

        JSONObject system = new JSONObject();
        JSONObject user = new JSONObject();
        system.put("role", "system");
        system.put("content", contentBuilder.toString() + "라고 했어.");
        user.put("role", "user");
        user.put("content", prompt);

        JSONArray messagesArray = new JSONArray();
        messagesArray.add(system);
        messagesArray.add(user);

        JSONObject json = new JSONObject();
        json.put("model", modelId);
        json.put("messages", messagesArray);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        log.info(request.toString());
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
        ChildEntity childEntity = childRepository.findByChildId(entity.getChildId());
        if (childEntity == null || !entity.getParentId().equals(childEntity.getParentId())) {
            log.warn("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }
    }
}
