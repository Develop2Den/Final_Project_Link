package com.finalProject.linkedin.dto.responce.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResp {
    private Long chatId;
    private Long senderId;
    private Long recipientId;
    private LocalDateTime createdAt;
}
