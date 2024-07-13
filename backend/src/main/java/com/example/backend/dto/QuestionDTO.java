package com.example.backend.dto;

import com.example.backend.model.QuestionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionDTO {
    private String questionId;
    private LocalDate date;
    private Long inputId;
    private String output;
    private String input;

    public QuestionDTO(QuestionEntity entity) {
        this.questionId = entity.getQuestionId();
        this.date = entity.getDate();
        this.inputId = entity.getInputId();
        this.output = entity.getOutput();
        this.input = entity.getInput();
    }

    public static QuestionEntity toEntity(QuestionDTO dto){
        return QuestionEntity.builder()
                .questionId(dto.getQuestionId())
                .date(dto.getDate())
                .inputId(dto.getInputId())
                .output(dto.getOutput())
                .input(dto.getInput())
                .build();
    }
}
