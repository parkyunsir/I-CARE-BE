package com.example.backend.dto;

import com.example.backend.model.ProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileDTO {
    private Long profileId;
    private File wordCloud;
    private LocalDateTime date;

    public ProfileDTO(ProfileEntity entity) {
        this.profileId = entity.getProfileId();
        this.wordCloud = entity.getWordCloud();
        this.date = entity.getDate();
    }
}
