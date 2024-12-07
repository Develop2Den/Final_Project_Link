package com.finalProject.linkedin.entity;

import com.finalProject.linkedin.utils.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * description
 *
 * @author Alexander Isai on 06.10.2024.
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    public Notification(LocalDateTime createdAt, String message, NotificationType notificationType, Long recipientId) {
        this.createdAt = createdAt;
        this.message = message;
        this.notificationType = notificationType;
        this.recipientId = recipientId;
        this.read = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "recipient_id")
    private Long recipientId;
    @Column(name = "message")
    private String message;
    @Column(name = "type")
    private NotificationType notificationType;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    @Column(name = "is_read")
    private Boolean read;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", insertable = false, updatable = false)
    private User recipient;

}
