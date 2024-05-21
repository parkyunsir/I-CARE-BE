package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.config.oauth.UserOAuth2Service;
import org.example.dto.ParentDto;
import org.example.entity.Parent;
import org.example.security.TokenProvider;
import org.example.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/parent")
public class ParentController {
    @Autowired
    private ParentService parentService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserOAuth2Service userOAuth2Service;

    final private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("")
    public ResponseEntity<?> signup(@RequestBody ParentDto parentDto) {
        try {
            if(parentDto == null || parentDto.getPassword() == null) {
                throw new RuntimeException("Invalid Password value.");
            }
            Parent parent = Parent.builder()
                    .email(parentDto.getEmail())
                    .password(passwordEncoder.encode(parentDto.getPassword()))
                    .nickname(parentDto.getNickname())
                    .build();
            Parent savedParent = parentService.signup(parent);
            ParentDto savedParentDto = ParentDto.builder()
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
    public ResponseEntity<?> login(@RequestBody ParentDto parentDto) {
        Parent parent = parentService.getByCredentials(
                parentDto.getEmail(),
                parentDto.getPassword(),
                passwordEncoder
        );
        if(parent != null) {
            final String token = tokenProvider.create(parent);
            final ParentDto loginParentDto = ParentDto.builder()
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
