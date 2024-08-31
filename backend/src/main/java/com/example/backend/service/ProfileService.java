package com.example.backend.service;

import com.example.backend.dto.DiaryMakerDTO;
import com.example.backend.model.ChildEntity;
import com.example.backend.model.DiaryEntity;
import com.example.backend.model.ProfileEntity;
import com.example.backend.repository.ChildRepository;
import com.example.backend.repository.DiaryRepository;
import com.example.backend.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private ChildRepository childRepository;

    public List<ProfileEntity> showList(String parentId, String childId) {
        validate(parentId, childId);
        return profileRepository.findByChildId(childId);
    }

    public ProfileEntity show(String parentId, String childId, String profileId) {
        validate(parentId, childId);
        if(!childId.equals(profileRepository.findByProfileId(profileId).getChildId())) {
            log.error("Not the owner of the profile");
            throw new RuntimeException("Not the owner of the profile");
        }
        return profileRepository.findByProfileId(profileId);
    }

    public Resource getImage(String parentId, String childId, String profileId) {
        try {
            ProfileEntity entity = show(parentId, childId, profileId);
            String fileName = entity.getWordCloud();

            String url = "http://127.0.0.1:5000/profile?fileName=" + fileName;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

            if(response.getStatusCode().is2xxSuccessful()) {
                ByteArrayResource resource = new ByteArrayResource(response.getBody());
                return resource;
            } else {
                throw new RuntimeException("Failed to retrieve image from Flask server");
            }
        } catch(Exception e) {
            throw new RuntimeException("Could not read file: ", e);
        }
    }

    public ProfileEntity create(String parentId, String childId) {
        validate(parentId, childId);
        ChildEntity originalChild = childRepository.findByChildId(childId);
        if(originalChild.getProfileState() < 5) {
            log.error("Profile state is insufficient. (profileState < 5)");
            throw new RuntimeException("Profile state is insufficient. (profileState < 5)");
        }
        String fileName = LocalDateTime.now().toString().replaceAll("[-:.]", "") +
                childId.substring(childId.length() - 5) + "_cloud.jpg";
        try {
            LocalDate threeMonthsAgo = LocalDate.now().minus(3, ChronoUnit.MONTHS);
            List<DiaryEntity> diaryList = diaryRepository.findByChildIdAndDateAfter(childId, threeMonthsAgo);

            String combinedContent = diaryList.stream()
                    .map(DiaryEntity::getContent)
                    .collect(Collectors.joining("\n"));

            DiaryMakerDTO dto = new DiaryMakerDTO(combinedContent, fileName);
            String url = "http://127.0.0.1:5000/profile";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String param = objectMapper.writeValueAsString(dto);
            HttpEntity<String> httpEntity = new HttpEntity<>(param, headers);
            restTemplate.postForObject(url, httpEntity, String.class);

            ProfileEntity entity = ProfileEntity.builder()
                    .profileId(null)
                    .parentId(parentId)
                    .childId(childId)
                    .wordCloud(fileName)
                    .date(LocalDateTime.now())
                    .build();
            originalChild.setProfileState(0);
            childRepository.save(originalChild);
            return profileRepository.save(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void validate(String parentId, String childId) {
        if(!parentId.equals(childRepository.findByChildId(childId).getParentId())) {
            log.error("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }
    }
}
