package com.finalProject.linkedin.service.serviceImpl;


import com.finalProject.linkedin.dto.request.notification.NotificationReq;
import com.finalProject.linkedin.dto.responce.notification.NotificationRes;
import com.finalProject.linkedin.entity.Notification;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.NotificationMapper;
import com.finalProject.linkedin.repository.NotificationRepository;
import com.finalProject.linkedin.service.serviceIR.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationRes create(NotificationReq notificationReq) {
        return notificationMapper.toNotificationRes(notificationMapper.toNotification(notificationReq));
    }

    @Override
    public boolean deleteById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id " + id));
        notification.setDeletedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        return true;
    }

    @Override
    public List<NotificationRes> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(notificationMapper::toNotificationRes).toList();
    }

    @Override
    public long countByRecipientIdReadFalse(Long userId) {
        return notificationRepository.countByRecipientIdAndReadFalse(userId);
    }

    @Override
    public List<NotificationRes> findByIdAndReadFalse(Pageable pageable, Long recipientId) {
        return notificationRepository.findByRecipientIdAndReadFalseOrderByCreatedAtDesc(pageable, recipientId)
                .map(notificationMapper::toNotificationRes).toList();
    }

    @Override
    public NotificationRes getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id " + id));
        return notificationMapper.toNotificationRes(notification);
    }

    @Override
    public boolean readTrue(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
        return true;
    }


}
