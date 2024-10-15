package com.finalProject.linkedin.dto.responce.notification;


import lombok.Data;
import lombok.NonNull;

@Data
public class NotificationRes {

    @NonNull
    private Long id;
    @NonNull
    private String message;
    private String notificationType;
    private Boolean read;

}
