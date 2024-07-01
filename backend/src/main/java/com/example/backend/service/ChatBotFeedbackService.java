package com.example.backend.service;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChatBotFeedbackEntity;
import com.example.backend.model.ChildEntity;
import com.example.backend.repository.ChatBotFeedbackRepository;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatBotFeedbackService {
    @Autowired
    ChatBotFeedbackRepository chatBotFeedbackRepository;

    @Autowired
    ChatBotRepository chatBotRepository;

    @Autowired
    ChildRepository childRepository;

    @Autowired
    ChatBotService chatBotService;

    @Value("${openai.api.key.b}")
    private String apiKey;

    @Value("${openai.model.id.b}")
    private String modelId;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatBotFeedbackEntity feedbackChat(String parentId, String childId) throws IOException {

        if(!childRepository.findByChildId(childId).getParentId().equals(parentId)) {
            log.warn("Not the owner of chattings");
            throw new RuntimeException("Not the owner of the chattings");
        }

        List<ChatBotEntity> chatbots = chatBotRepository.findByChildId(childId); // 챗봇 리스트

        String request = chatbots.stream().map(ChatBotEntity::getRequest).collect(Collectors.joining(" ")); // request 뽑아서 List<String> 으로 만들기

        String response = getCompletion("\'" + request+"\' 부모가 자녀에게 이런 말을 했어. 이에 대한 피드백을 줘.");

        String parentRequest = chatbots.stream()
                .map(ChatBotEntity::getRequest)
                .max(Comparator.comparingInt(String::length))
                .orElse("");

        ChatBotFeedbackEntity entity = ChatBotFeedbackEntity.builder()
                .parentId(parentId)
                .childId(childId)
                .date(LocalDateTime.now())
                .feedback(response)
                .parentRequest(parentRequest)
                .build();
        chatBotService.delete(parentId, childId);

        return chatBotFeedbackRepository.save(entity);
    }

    public List<ChatBotFeedbackEntity> feedbackList(String childId, String parentId){
        //검증
        if(!childRepository.findByChildId(childId).getParentId().equals(parentId)) {
            log.warn("Not the owner of feedback");
            throw new RuntimeException("Not the owner of the feedback");
        }

        return chatBotFeedbackRepository.findByChildId(childId);
    }


    // chatbot - request 가져오기
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


    // 유효성 검사
    public void validate(ChatBotFeedbackEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getChatBotFeedbackId() != null) { // entity, original의 child 인증
        ChatBotFeedbackEntity original = chatBotFeedbackRepository.findByChatBotFeedbackId(entity.getChatBotFeedbackId());
        if(!original.getChildId().equals(entity.getChildId())) {
            log.warn("Not the owner of the diary");
            throw new RuntimeException("Not the owner of the diary");
        }
        }
//        ChildEntity childEntity = childRepository.findByChildId(entity.getChildId());
//        if (childEntity == null || !entity.getParentId().equals(childEntity.getParentId())) {
//            log.warn("Child's parent and current parent do not match.");
//            throw new RuntimeException("Child's parent and current parent do not match.");
//        }
}


}
