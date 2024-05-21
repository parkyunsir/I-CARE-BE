package com.example.backend.repository;

import com.example.backend.model.IconEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends JpaRepository<IconEntity, Long> {
    IconEntity findByIconId(Long iconId);
}
