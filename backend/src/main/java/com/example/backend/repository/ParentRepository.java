package com.example.backend.repository;

import com.example.backend.model.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, String> {
    Optional<ParentEntity> findOptionalByEmail(String email);
    ParentEntity findByEmail(String email);
}
