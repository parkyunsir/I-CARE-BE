package com.example.backend.service;

import com.example.backend.model.ParentEntity;
import com.example.backend.repository.ParentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParentService {

    @Autowired
    private ParentRepository parentRepository;

    public ParentEntity create(final ParentEntity parentEntity){
        if(parentEntity == null || parentEntity.getNickname() == null){
            throw new RuntimeException("Invalid arguments");
        }

        final String nickname = parentEntity.getNickname();

        if(parentRepository.existsByNickname(nickname)){
            log.warn("Username already exists {}", nickname);
            throw new RuntimeException("Nickname already exists");
        }
        return parentRepository.save(parentEntity);
    }

    /*
    public UserEntity getByCredential(final String username, final String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }
    */


    // 패스워드를 string으로 선언하면 안좋다 -> 암호화
    public ParentEntity getByCredential(final String nickname, final String password, final PasswordEncoder encoder){

        final ParentEntity originalUser = parentRepository.findByNickname(nickname);
        // matches 메서드로 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }

}