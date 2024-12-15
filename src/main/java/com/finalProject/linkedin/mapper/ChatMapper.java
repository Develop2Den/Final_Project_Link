package com.finalProject.linkedin.mapper;

import com.finalProject.linkedin.dto.request.chat.ChatReq;
import com.finalProject.linkedin.dto.responce.chat.ChatResp;
import com.finalProject.linkedin.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Chat toChat(ChatReq chatReq);

    ChatResp toChatResp(Chat chat);
}
