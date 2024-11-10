package com.finalProject.linkedin.dto.request.notification;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finalProject.linkedin.utils.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationReq {


    @NotNull(message = "Recipient field must be filled ")
    private Long recipientId;
    @NotBlank(message = "Message can't be empty")
    private String message;
    private NotificationType notificationType;

}
