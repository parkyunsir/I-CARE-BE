package com.example.backend.controller;

import com.example.backend.dto.DiaryDTO;
import com.example.backend.model.DiaryEntity;
import com.example.backend.repository.DiaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DiaryControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private DiaryRepository diaryRepository;

    @DisplayName("createDiary: 일기 추가에 성공한다.")
    @Test
    public void createDiary() throws Exception {
        final String url = "/api/diary";
        final String childId = "temporary-childId";
        final String content = "temporary-content";
        final Long iconId = 1L;
        final DiaryDTO dto = DiaryDTO.builder()
                .content(content)
                .iconId(iconId)
                .build();

        final String requestParam = "?childId=" + childId;
        final String requestBody = objectMapper.writeValueAsString(dto);

        final ResultActions result = mockMvc.perform(post(url + requestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isOk());
    }

    @DisplayName("showDiaryDetail : 일기 자세히 보기에 성공한다.")
    @Test
    public void showDiaryDetail() throws Exception {
        DiaryEntity savedEntity = saveDiaryEntityOnRepository();
        final String url = "/api/diary";
        final String diaryId = savedEntity.getDiaryId();
        final String childId = "temporary-childId";

        final String requestParam = "?childId=" + childId + "&diaryId=" + diaryId;

        final ResultActions result = mockMvc.perform(get(url + requestParam)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andDo(print());
    }

    @DisplayName("showDiaryMonthlyList : 일기 월별 리스트 보기에 성공한다.")
    @Test
    public void showDiaryMonthlyList() throws Exception {
        DiaryEntity savedEntity = saveDiaryEntityOnRepository();
        final String url = "/api/diary/list";
        final String childId = "temporary-childId";

        final String requestParam = "?childId=" + childId + "&month=" + 5;

        final ResultActions result = mockMvc.perform(get(url + requestParam)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andDo(print());
    }

    @DisplayName("updateDiary: 일기 수정에 성공한다.")
    @Test
    public void updateDiary() throws Exception {
        DiaryEntity savedEntity = saveDiaryEntityOnRepository();
        final String url = "/api/diary";
        final String diaryId = savedEntity.getDiaryId();
        final String childId = "temporary-childId";

        final String updatedContent = "update-temporary-content";
        final DiaryDTO dto = DiaryDTO.builder()
                .diaryId(diaryId)
                .content(updatedContent)
                .build();
        final String requestParam = "?childId=" + childId;
        final String requestBody = objectMapper.writeValueAsString(dto);

        final ResultActions result = mockMvc.perform(put(url + requestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isOk());

        DiaryEntity updatedEntity = diaryRepository.findByDiaryId(diaryId);
        assertThat(updatedEntity.getContent()).isEqualTo(updatedContent);
    }

    @DisplayName("deleteDiary: 일기 삭제에 성공한다.")
    @Test
    public void deleteDiary() throws Exception {
        DiaryEntity savedEntity = saveDiaryEntityOnRepository();
        final String url = "/api/diary";
        final String diaryId = savedEntity.getDiaryId();
        final String childId = "temporary-childId";
        final DiaryDTO dto = DiaryDTO.builder()
                .diaryId(savedEntity.getDiaryId())
                .date(savedEntity.getDate())
                .content(savedEntity.getContent())
                .iconId(savedEntity.getIconId())
                .build();

        final String requestParam = "?childId=" + childId;
        final String requestBody = objectMapper.writeValueAsString(dto);

        final ResultActions result = mockMvc.perform(delete(url + requestParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isOk());

        DiaryEntity deletedEntity = diaryRepository.findByDiaryId(diaryId);
        assertThat(deletedEntity).isEqualTo(null);
    }

    public DiaryEntity saveDiaryEntityOnRepository() {
        final String diaryId = "temporary-diaryId";
        final String parentId = "temporary-parentId";
        final String childId = "temporary-childId";
        final LocalDate date = LocalDate.now();
        final String content = "temporary-content";
        final Long iconId = 1L;
        DiaryEntity entity = DiaryEntity.builder()
                .diaryId(diaryId)
                .parentId(parentId)
                .childId(childId)
                .date(date)
                .content(content)
                .iconId(iconId)
                .build();
        return diaryRepository.save(entity);
    }
}
