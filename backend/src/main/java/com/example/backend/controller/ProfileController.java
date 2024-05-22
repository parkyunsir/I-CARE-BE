package com.example.backend.controller;

import com.example.backend.dto.ProfileDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.ProfileEntity;
import com.example.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<?> createProfile(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        try {
            //String parentId = "temporary-parentId";
            ProfileEntity entity = profileService.create(parentId, childId);
            ProfileDTO dto = new ProfileDTO(entity);
            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<ProfileDTO> response = ResponseDTO.<ProfileDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> showProfileList(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        //String parentId = "temporary-parentId";
        List<ProfileEntity> entities = profileService.showList(parentId, childId);
        List<ProfileDTO> dtos = entities.stream().map(ProfileDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }
}
