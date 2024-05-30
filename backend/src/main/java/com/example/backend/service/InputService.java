package com.example.backend.service;

import com.example.backend.model.InputEntity;
import com.example.backend.repository.InputRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InputService {
    @Autowired
    private InputRepository inputRepository;

    // 상단에 출력
    public InputEntity show(LocalDate date){
        return inputRepository.findByDate(date);
    }

    // 리스트에서 출력
    public List<InputEntity> offerList(LocalDate date){
        return inputRepository.findAll();
    }
}
