package com.example.backend.dto;

import com.example.backend.model.InputEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InputDTO {
    private Long inputId;
    private String content;

    public InputDTO(InputEntity entity) {
        this.inputId = entity.getInputId();
        this.content = entity.getContent();
    }
}
