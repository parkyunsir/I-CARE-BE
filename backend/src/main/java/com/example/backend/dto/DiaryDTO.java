package com.example.backend.dto;

import com.example.backend.model.DiaryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiaryDTO {
    private String diaryId;
    private LocalDate date;
    private String content;
    private Long iconId;

    public DiaryDTO(DiaryEntity entity) {
        this.diaryId = entity.getDiaryId();
        this.date = entity.getDate();
        this.content = entity.getContent();
        this.iconId = entity.getIconId();
    }

    public static DiaryEntity toEntity(DiaryDTO dto) {
        return DiaryEntity.builder()
                .diaryId(dto.getDiaryId())
                .date(dto.getDate())
                .content(dto.getContent())
                .iconId(dto.getIconId())
                .build();
    }
}
