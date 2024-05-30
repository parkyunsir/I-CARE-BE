package com.example.backend.repository;

import com.example.backend.model.InputEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InputRepository extends JpaRepository<InputEntity, String> {

    InputEntity findByDate(LocalDate date); //날짜로 질문 내역 가져오기
}
