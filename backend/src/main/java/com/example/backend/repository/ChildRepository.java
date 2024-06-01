package com.example.backend.repository;

import com.example.backend.model.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<ChildEntity, String> {
    List<ChildEntity> findByParentId(String parentId);
    Optional<ChildEntity> findByChildId(String childId);
}

