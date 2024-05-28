package com.example.backend.controller;

import com.example.backend.dto.ProfileDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.ProfileEntity;
import com.example.backend.service.ProfileService;
import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/list")
    public ResponseEntity<?> showProfileList(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId) {
        //String parentId = "temporary-parentId";
        List<ProfileEntity> entities = profileService.showList(parentId, childId);
        List<ProfileDTO> dtos = entities.stream().map(ProfileDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping
    public ResponseEntity<?> showProfile(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestParam("profileId") String profileId) {
        //String parentId = "temporary-parentId";
        ProfileEntity entity = profileService.show(parentId, childId, profileId);
        ProfileDTO dto = new ProfileDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/image")
    public ResponseEntity<?> getProfileImage(@AuthenticationPrincipal String parentId, @RequestParam("childId") String childId, @RequestParam("profileId") String profileId) {
        //String parentId = "temporary-parentId";
        Resource resource = profileService.getImage(parentId, childId, profileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

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
}
