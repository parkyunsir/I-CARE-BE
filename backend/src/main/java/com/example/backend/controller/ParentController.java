package com.example.backend.controller;

import com.example.backend.config.oauth.UserOAuth2Service;
import com.example.backend.dto.ParentDTO;
import com.example.backend.model.ParentEntity;
import com.example.backend.security.TokenProvider;
import com.example.backend.service.ParentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/parent")
public class ParentController {
    @Autowired
    private ParentService parentService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserOAuth2Service userOAuth2Service;

    final private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("")
    public ResponseEntity<?> signup(@RequestBody ParentDTO parentDto) {
        try {
            if(parentDto == null || parentDto.getPassword() == null) {
                throw new RuntimeException("Invalid Password value.");
            }
            ParentEntity parent = ParentEntity.builder()
                    .email(parentDto.getEmail())
                    .password(passwordEncoder.encode(parentDto.getPassword()))
                    .nickname(parentDto.getNickname())
                    .build();
            ParentEntity savedParent = parentService.signup(parent);
            ParentDTO savedParentDto = ParentDTO.builder()
                    .id(savedParent.getId())
                    .email(savedParent.getEmail())
                    .nickname(savedParent.getNickname())
                    .build();
            return ResponseEntity.ok().body(savedParentDto);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ParentDTO parentDto) {
        ParentEntity parent = parentService.getByCredentials(
                parentDto.getEmail(),
                parentDto.getPassword(),
                passwordEncoder
        );
        if(parent != null) {
            final String token = tokenProvider.create(parent);
            final ParentDTO loginParentDto = ParentDTO.builder()
                    .id(parent.getId())
                    .email(parent.getEmail())
                    .nickname(parent.getNickname())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(loginParentDto);
        } else {
            return ResponseEntity.badRequest().body("Login failed");
        }
    }

    @GetMapping("/kakao/nickname")
    public ResponseEntity<?> getKakaoNickname() {
        log.info(userOAuth2Service.getParentNickname());
        return ResponseEntity.ok().body(userOAuth2Service.getParentNickname());
    }
}

