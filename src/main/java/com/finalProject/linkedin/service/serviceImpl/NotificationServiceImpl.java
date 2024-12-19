package com.finalProject.linkedin.service.serviceImpl;


import com.finalProject.linkedin.entity.Notification;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.repository.NotificationRepository;
import com.finalProject.linkedin.repository.UserRepository;
import com.finalProject.linkedin.service.serviceIR.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class
NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public Notification create(Notification notification) {
        notificationRepository.save(notification);
        notification.setAuthor(userRepository.getReferenceById(notification.getAuthorId()));
        return notification;
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
    public Page<Notification> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Long countByRecipientIdReadFalse(Long userId) {
        return notificationRepository.countByRecipientIdAndDeletedAtIsNullAndReadFalse(userId);
    }

    @Override
    public Page<Notification> findByIdAndReadFalse(Long recipientId, Pageable pageable) {
        return notificationRepository.findByRecipientIdAndDeletedAtIsNullAndReadFalseOrderByCreatedAtDesc(recipientId,pageable);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id " + id));
    }

    @Override
    public boolean readTrue(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
        return true;
    }

    @Override
    public Page<Notification> findByIdAndDeleteFalse(Long id, Pageable pageable) {
        return notificationRepository.findByRecipientIdAndDeletedAtIsNullOrderByCreatedAtDesc(id,pageable);
    }


}
