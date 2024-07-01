package com.example.backend.dto;

import com.example.backend.model.ChatBotFeedbackEntity;
import com.example.backend.model.ChildEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildDTO {

    private String childId;
    private String parentId;
    private String nickname;
    private String name;
    private String gender;
    private Date birth;
    private int profileState;
    public ChildDTO(ChildEntity entity) {
        this.childId= entity.getChildId();
        this.parentId = entity.getParentId();
        this.nickname = entity.getNickname();
        this.name = entity.getName();
        this.gender = entity.getGender();
        this.birth = entity.getBirth();
        this.profileState = entity.getProfileState();
    }

    public static ChildEntity toEntity(final ChildDTO dto) {
        return ChildEntity.builder()
                .childId(dto.getChildId())
                .parentId(dto.getParentId())
                .nickname(dto.getNickname())
                .name(dto.getName())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .profileState(dto.getProfileState())
                .build();
    }


}
