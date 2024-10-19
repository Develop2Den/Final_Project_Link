package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository  extends JpaRepository<Notification, Long> {

}
