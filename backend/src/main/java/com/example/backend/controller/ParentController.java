package com.example.backend.controller;

import com.example.backend.dto.ParentDTO;
import com.example.backend.dto.ResponseDTO;
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

@Slf4j
@RestController
@RequestMapping("/auth")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @Autowired
    private TokenProvider tokenProvider;

    // BCryptPasswordEncoder() 스프링 시큐리티가 제공하는 패스워드 암호화 클래스
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup") // 회원가입
    public ResponseEntity<?> registerUser(@RequestBody ParentDTO parentDTO){
        try{
            if(parentDTO == null || parentDTO.getPassword() == null){
                throw new RuntimeException("Invalid password value");
            }

            ParentEntity parent = ParentEntity.builder()
                    .nickname(parentDTO.getNickname())
                    .password(passwordEncoder.encode(parentDTO.getPassword()))
                    .build();

            ParentEntity registeredUser = parentService.create(parent);

            ParentDTO reponseUserDTO = ParentDTO.builder()
                    .parentId(registeredUser.getParentId())
                    .nickname(registeredUser.getNickname())
                    .build();
            return ResponseEntity.ok().body(reponseUserDTO);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
    @PostMapping("/signin") // 로그인
    public ResponseEntity<?> authenticate(@RequestBody ParentDTO parentDTO){
        ParentEntity parent = parentService.getByCredential(
                parentDTO.getNickname(),
                parentDTO.getPassword(),
                passwordEncoder);
        if(parent != null){
            final String token = tokenProvider.create(parent);
            final ParentDTO responseUserDTO = ParentDTO.builder()
                    .nickname(parent.getNickname())
                    .parentId(parent.getParentId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }else{
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
