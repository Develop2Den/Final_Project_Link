package com.finalProject.linkedin.service.serviceImpl;


import com.finalProject.linkedin.dto.request.notification.NotificationReq;
import com.finalProject.linkedin.dto.responce.notification.NotificationRes;
import com.finalProject.linkedin.entity.Notification;
import com.finalProject.linkedin.mapper.NotificationMapper;
import com.finalProject.linkedin.repository.NotificationRepository;
import com.finalProject.linkedin.service.serviceIR.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;

    @Override
    public NotificationRes create(NotificationReq notificationReq) {
        Notification notification = notificationMapper.toNotification(notificationReq);
        if (getIdFromEntity(notification) == 0L) {
            Notification savedNotification = notificationRepository.save(notification);
            return notificationMapper.toNotificationRes(savedNotification);
        }
        throw new RuntimeException("Notification already exist");
    }

    @Override
    public boolean deleteById(Long id) {
        if (notificationRepository.existsById(id)) {
            Notification notification = getOne(id);
            notification.setDeletedAt(LocalDateTime.now());
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

    @Override
    public Notification getOne(long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<NotificationRes> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(notificationMapper::toNotificationRes).toList();
    }

    @Override
    public long countByRecipientIdReadFalse(Long id) {
        return notificationRepository.countByRecipientIdAndReadFalse(id);
    }

    @Override
    public List<NotificationRes> findByIdAndReadFalse(Pageable pageable, Long recipientId) {
        return notificationRepository.findByRecipientIdAndReadFalseOrderByCreatedAtDesc(pageable, recipientId)
                .map(notificationMapper::toNotificationRes).toList();
    }


    public long getIdFromEntity(Notification notification) {
        Optional<Notification> notification1 = notificationRepository.findAll().stream().filter(c -> c.equals(notification)).findFirst();
        return notification1.map(Notification::getNotificationId).orElse(0L);
    }

}
