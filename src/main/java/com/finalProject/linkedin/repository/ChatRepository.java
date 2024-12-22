package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE ((c.senderId = :senderId AND c.recipientId = :recipientId) " +
            "OR (c.senderId = :recipientId AND c.recipientId = :senderId)) AND c.deletedAt IS NULL")
    Optional<Chat> findChatByParticipants(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId);


}
