package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiaryMakerDTO {
    private String diary;
    private String fileName;

    public DiaryMakerDTO(String diary) {
        this.diary = diary;
    }
}
