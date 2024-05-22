package com.example.backend.repository;

import com.example.backend.model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    @Query(value = "SELECT * FROM Profile WHERE child_id = :childId ORDER BY date DESC", nativeQuery = true)
    List<ProfileEntity> findByChildId(@Param("childId") String childId);
}
