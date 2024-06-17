package com.example.backend.service;

import com.example.backend.model.ChildEntity;
import com.example.backend.repository.ChildRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChildService {
    @Autowired
    private ChildRepository repository;

    public List<ChildEntity> create(final ChildEntity entity) {
        validate(entity); // 데이터가 유효한지 검증
        repository.save(entity); // 엔터티 리파지토리에 저장
        log.info("Entity ChildId: {} is saved", entity.getChildId());
        return repository.findByParentId(entity.getParentId()); // 부모 ID로 저장된 모든 자녀 엔터티를 찾아서 리턴
    }

    public List<ChildEntity> showList(String parentId) {
        return repository.findByParentId(parentId);
    }

    public ChildEntity show(String parentId, String childId) {
        validateFamily(parentId, childId);
        return repository.findByChildId(childId); // Optional을 사용하지 않고 ChildEntity를 반환
    }

    public void validateFamily(String parentId, String childId) {
        ChildEntity child = repository.findByChildId(childId);
        if (child == null || !parentId.equals(child.getParentId())) {
            log.error("Child's parent and current parent do not match.");
            throw new RuntimeException("Child's parent and current parent do not match.");
        }
    }

    public void validate(ChildEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
    }
}
