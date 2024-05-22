package com.example.backend.repository;

import com.example.backend.model.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, String> {

    ParentEntity findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    ParentEntity findByNicknameAndPassword(String nickname, String password);
}
