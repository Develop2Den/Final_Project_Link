package com.finalProject.linkedin.dto.responce.message;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResp {

    private Long messageId;
    private Long chatId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean read;
}
