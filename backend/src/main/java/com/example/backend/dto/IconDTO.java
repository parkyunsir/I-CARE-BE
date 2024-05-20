package com.example.backend.dto;

import com.example.backend.model.IconEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IconDTO {
    private Long iconId;
    private String font;

    public IconDTO(IconEntity entity) {
        this.iconId = entity.getIconId();
        this.font = entity.getFont();
    }
}
