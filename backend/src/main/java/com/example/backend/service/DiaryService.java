package com.example.backend.service;

import com.example.backend.model.DiaryEntity;
import com.example.backend.repository.DiaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class DiaryService {
    @Autowired
    DiaryRepository diaryRepository;

    public DiaryEntity create(DiaryEntity entity) {
        validate(entity);
        if(diaryRepository.existsByDate(entity.getDate())) {
            log.warn("A diary for that date already exists.");
            throw new RuntimeException("A diary for that date already exists.");
        }
        DiaryEntity savedEntity = diaryRepository.save(entity);
        log.info("Entity Id : {} is saved.", savedEntity.getDiaryId());
        return diaryRepository.findByDiaryId(savedEntity.getDiaryId());
    }

    public DiaryEntity showDetail(String parentId, String childId, String diaryId) {
        DiaryEntity entity = diaryRepository.findByDiaryId(diaryId);
        if(!entity.getParentId().equals(parentId) || !entity.getChildId().equals(childId)) {
            log.error("Not the owner of the diary");
            throw new RuntimeException("Not the owner of the diary");
        }
        return entity;
    }

    public List<DiaryEntity> showMonthlyList(String parentId, String childId, Long month) {
        /*if(parentId.equals(childRepository.findBychildId(childId).getParentId())) {
            log.error("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }*/
        return diaryRepository.findByDateMonth(month);
    }

    public DiaryEntity update(DiaryEntity entity) {
        validate(entity);
        DiaryEntity original = diaryRepository.findByDiaryId(entity.getDiaryId());
        original.setContent(entity.getContent());
        original.setIconId(entity.getIconId());
        diaryRepository.save(original);
        return diaryRepository.findByDiaryId(original.getDiaryId());
    }

    public LocalDate delete(DiaryEntity entity) {
        validate(entity);
        LocalDate date = entity.getDate();
        try {
            diaryRepository.delete(entity);
        } catch(Exception e) {
            log.error("error deleting entity ", entity.getDiaryId(), e);
            throw new RuntimeException("error deleting entity " + entity.getDiaryId());
        }
        return date;
    }

    public void validate(DiaryEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getParentId() == null || entity.getChildId() == null) {
            log.warn("Unknown parent or child.");
            throw new RuntimeException("Unknown parent or child.");
        }/*
        if(!entity.getParentId().equals(childRepository.findByChildId(entity.getChildId()).getParentId())) { // entity의 parent, child 인증
            log.warn("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }*/
        if(entity.getDiaryId() != null) { // entity, original의 child 인증
            DiaryEntity original = diaryRepository.findByDiaryId(entity.getDiaryId());
            if(!original.getChildId().equals(entity.getChildId())) {
                log.warn("Not the owner of the diary");
                throw new RuntimeException("Not the owner of the diary");
            }
        }
    }
}
