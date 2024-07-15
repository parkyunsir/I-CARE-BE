package com.example.backend.repository;

import com.example.backend.model.InputEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InputRepository extends JpaRepository<InputEntity, String> {

    InputEntity findByInputId(Long inputId);
}
