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
    private String iconImage;

    public IconDTO(IconEntity entity) {
        this.iconId = entity.getIconId();
        this.iconImage = entity.getIconImage();
    }
}
