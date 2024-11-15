package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByRecipientIdAndReadFalse(long recipientId);

    Page<Notification> findByRecipientIdAndReadFalseOrderByCreatedAtDesc(Pageable pageable, Long recipientId);
}
