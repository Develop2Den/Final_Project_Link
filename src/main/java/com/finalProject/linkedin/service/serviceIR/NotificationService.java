package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.request.notification.NotificationReq;
import com.finalProject.linkedin.dto.responce.notification.NotificationRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    NotificationRes create(NotificationReq notificationReq);

    boolean deleteById(Long id);

    List<NotificationRes> findAll(Pageable pageable);

    long countByRecipientIdReadFalse(Long recipientId);

    List<NotificationRes> findByIdAndReadFalse(Pageable pageable, Long recipientId);

    NotificationRes getNotificationById(Long id);

    boolean readTrue(Long id);
}
