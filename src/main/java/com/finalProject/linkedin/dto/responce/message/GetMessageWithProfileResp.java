package com.finalProject.linkedin.dto.responce.message;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetMessageWithProfileResp {
    private Long chatId;
    private Long messageId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private String name;
    private String surname;
    private String headerPhotoUrl;
    private LocalDateTime createdAt;
    private Boolean read;
    private long unreadMessagesCount;

}
