package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m " +
            "WHERE ((m.senderId = :userId1 AND m.recipientId = :userId2) " +
            "OR (m.senderId = :userId2 AND m.recipientId = :userId1)) " +
            "AND m.deletedAt IS NULL " +
            "ORDER BY m.createdAt DESC ")
    Page<Message> findMessagesBetweenUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2,
            Pageable pageable);


    @Query("SELECT m FROM Message m " +
            "JOIN (SELECT LEAST(m1.senderId, m1.recipientId) AS user1, " +
            "GREATEST(m1.senderId, m1.recipientId) AS user2, " +
            "MAX(m1.createdAt) AS latestMessageTime " +
            "FROM Message m1 " +
            "WHERE m1.deletedAt IS NULL " +
            "AND (m1.senderId = :id OR m1.recipientId = :id) " +  // Фильтрация по id
            "GROUP BY user1, user2) grouped " +
            "ON (LEAST(m.senderId, m.recipientId) = grouped.user1 " +
            "AND GREATEST(m.senderId, m.recipientId) = grouped.user2 " +
            "AND m.createdAt = grouped.latestMessageTime) " +
            "ORDER BY m.createdAt DESC")
    Page<Message> findLatestMessagesForEachPairByUserId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m " +
            "WHERE m.read = false AND " +
            "(m.senderId = :userId2 AND m.recipientId = :userId1) " +
            "AND m.deletedAt IS NULL")
    long countUnreadMessagesBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);


}
