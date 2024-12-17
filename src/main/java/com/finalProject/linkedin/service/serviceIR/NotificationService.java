package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.responce.notification.NotificationRes;
import com.finalProject.linkedin.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    Notification create(Notification notification);

    boolean deleteById(Long id);

    Page<Notification> findAll(Pageable pageable);

    Long countByRecipientIdReadFalse(Long recipientId);

    Page<Notification> findByIdAndReadFalse(Long recipientId, Pageable pageable);

    Notification getNotificationById(Long id);

    boolean readTrue(Long id);

    Page<Notification> findByIdAndDeleteFalse(Long id, Pageable pageable);
}
