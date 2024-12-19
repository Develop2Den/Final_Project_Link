package com.finalProject.linkedin.mapper;

import com.finalProject.linkedin.dto.request.notification.NotificationReq;
import com.finalProject.linkedin.dto.responce.notification.NotificationRes;
import com.finalProject.linkedin.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "read", constant = "false")
    Notification toNotification(NotificationReq notificationReq);


    @Mapping(target = "nameSender", expression = "java(notification.getAuthor().getProfile().getName())")
    @Mapping(target = "surnameSender", expression = "java(notification.getAuthor().getProfile().getSurname())")
    NotificationRes toNotificationRes(Notification notification);
}
