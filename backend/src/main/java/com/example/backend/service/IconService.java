package com.example.backend.service;

import com.example.backend.model.IconEntity;
import com.example.backend.repository.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IconService {
    @Autowired
    private IconRepository iconRepository;
/*
    public List<IconEntity> offerList(String content) {

    }*/

    public IconEntity show(Long iconId) {
        return iconRepository.findByIconId(iconId);
    }
}
