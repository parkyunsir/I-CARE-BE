package com.example.backend.service;

import com.example.backend.model.InputEntity;
import com.example.backend.model.QuestionEntity;
import com.example.backend.repository.InputRepository;
import com.example.backend.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;


@Slf4j
@Service
public class InputService {
    @Autowired
    private InputRepository inputRepository;

    public InputEntity showInput(Long inputId) { // 불러오기용
        return inputRepository.findByInputId(inputId);
    }
}
