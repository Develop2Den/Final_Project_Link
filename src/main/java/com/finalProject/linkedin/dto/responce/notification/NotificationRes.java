package com.finalProject.linkedin.dto.responce.notification;


import com.finalProject.linkedin.utils.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRes {

    private Long notificationId;
    private Long eventId;
    private Long recipientId;
    private String message;
    private NotificationType notificationType;
    private LocalDateTime createdAt;
    private Boolean read;

}
