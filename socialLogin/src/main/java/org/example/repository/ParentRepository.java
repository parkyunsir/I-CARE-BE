package org.example.repository;

import org.example.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, String> {
    Optional<Parent> findOptionalByEmail(String email);

    Parent findByEmail(String email);
}
