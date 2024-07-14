package com.example.backend.dto;

import com.example.backend.model.InputEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InputDTO {
    private Long inputId;
    private String input;

    public InputDTO(InputEntity entity) {
        this.inputId = entity.getInputId();
        this.input = entity.getInput();
    }

    public static InputEntity toEntity(InputDTO dto) {
        return InputEntity.builder()
                .inputId(dto.getInputId())
                .input(dto.getInput())
                .build();
    }
}
