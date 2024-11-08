package com.finalProject.linkedin.mapper;

import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.GetMessageWithProfileResp;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import com.finalProject.linkedin.entity.Message;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "name", expression = "java(message.getSenderId().equals(userId) " +
            "? message.getRecipient().getProfile().getName() : message.getSender().getProfile().getName())")
    @Mapping(target = "surname", expression = "java(message.getSenderId().equals(userId) " +
            "? message.getRecipient().getProfile().getSurname() : message.getSender().getProfile().getSurname())")
    @Mapping(target = "headerPhotoUrl", expression = "java(message.getSenderId().equals(userId) " +
            "? message.getRecipient().getProfile().getHeaderPhotoUrl() : message.getSender().getProfile().getHeaderPhotoUrl())")

   GetMessageWithProfileResp toMessageWithUserResp(Message message, @Context Long userId);

    List<GetMessageWithProfileResp> toMessageWithUserRespList(List<Message> messages, @Context Long userId);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Message toMessage(MessageReq messageReq);

    MessageResp toMessageResp(Message Message);
}

