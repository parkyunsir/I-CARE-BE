package com.example.backend.repository;

import com.example.backend.model.ChatBotEntity;
import com.example.backend.model.ChatBotFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatBotRepository extends JpaRepository<ChatBotEntity, String> {
    List<ChatBotEntity> findByChildId(String childId);

    //@Query(value = "DELETE FROM CHAT_BOT WHERE parent_id = :parentId AND child_id = :childId", nativeQuery = true)
    //void deleteByParentIdAndChildId(@Param("parentId") String parentId, @Param("childId") String childId);

    void deleteByParentIdAndChildId(String parentId, String childId);
}
