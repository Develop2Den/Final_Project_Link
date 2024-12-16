package com.finalProject.linkedin.dto.request.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatReq {

    @NotNull(message = "Sender field must be filled ")
    private Long senderId;

    @NotNull(message = "Recipient field must be filled ")
    private Long recipientId;
}
