package com.finalProject.linkedin.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageChatIdReq {
   
    private Long chatId;
    @NotNull
    private Long senderId;
    @NotNull
    private Long recipientId;
    @NotBlank
    private String content;
}