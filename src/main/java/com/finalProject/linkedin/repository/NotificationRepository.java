package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countByRecipientIdAndDeletedAtIsNullAndReadFalse(Long recipientId);

    Page<Notification> findByRecipientIdAndDeletedAtIsNullAndReadFalseOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

    Page<Notification> findByRecipientIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

}
