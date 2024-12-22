package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.message.MessageChatIdReq;
import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.GetMessageWithProfileResp;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import com.finalProject.linkedin.mapper.MessageMapper;
import com.finalProject.linkedin.service.serviceIR.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;


    @Operation(summary = "Show method not for production - Get paginated messages", description = "Get list of messages with pagination")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list")
    public ResponseEntity<List<MessageResp>> getAllMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request chats: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(messageService.findAll(pageable).map(messageMapper::toMessageResp).toList());
    }

    @Operation(summary = "Create new message", description = "Creates a new message")
    @ApiResponse(responseCode = "201")
    @PostMapping("/create")
    public ResponseEntity<MessageResp> createMessage(@Valid @RequestBody MessageReq messageReq) {
        return ResponseEntity.ok(messageMapper.toMessageResp(messageService.createAndSendOrNotification(messageMapper.toMessage(messageReq), 1)));
    }

    @Operation(summary = "Create new message with chat id", description = "Creates a new message with chat id")
    @ApiResponse(responseCode = "201")
    @PostMapping("/create/identity")
    public ResponseEntity<MessageResp> createMessage1(@Valid @RequestBody MessageChatIdReq messageChatIdReq) {
        return ResponseEntity.ok(messageMapper.toMessageResp(messageService.createAndSendOrNotification(messageMapper.toMessage(messageChatIdReq), 2)));
    }

    @MessageMapping("/chat/{chatId}/send")
    @SendTo("/queue/messages/{chatId}")
    public MessageResp sendMessage(@DestinationVariable String chatId, MessageChatIdReq messageChatIdReq) {
        return messageMapper.toMessageResp(messageService.createAndSendOrNotification(messageMapper.toMessage(messageChatIdReq), 3));
    }

    @Operation(summary = "Mark message as deleted",
            description = "Mark message as logically deleted by setting 'deletedAt'")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable long id) {
        if (messageService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get paginated messages between two users",
            description = "Get list of messages with pagination between two users by id, sorted by created date ")
    @ApiResponse(responseCode = "200")
    @GetMapping("/chat")
    public ResponseEntity<List<MessageResp>> getAllMessagesChat(
            @RequestParam long id1,
            @RequestParam long id2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(messageService.getChatMessages(id1, id2, pageable).map(messageMapper::toMessageResp).toList());
    }

    @Operation(summary = "Get paginated messages between two different users", description = "Get list of messages with pagination between user by id and other users. 1 message for each pair")
    @ApiResponse(responseCode = "200")
    @GetMapping("/latest")
    public ResponseEntity<List<GetMessageWithProfileResp>> getAllMessagesLatest(
            @RequestParam long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(messageService.findLatestMessagesForUser(id, pageable));
    }

    @Operation(summary = "Get paginated messages between two  users by chat id", description = "Get list of messages with pagination between user by chat id  ")
    @ApiResponse(responseCode = "200")
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<MessageResp>> getAllMessagesChatId(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(messageService.getMessagesByChatId(id, pageable).map(messageMapper::toMessageResp).toList());
    }

    @PutMapping("/read/{id}")
    @Operation(summary = "mark message as read", description = "mark message as read by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> setMessageRead(@PathVariable Long id) {
        if (messageService.readTrue(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/read")
    @Operation(summary = "mark messages as read", description = "mark messages as read by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> setMessagesRead(@RequestBody List<Long> ids) {
        if (messageService.readTrue(ids)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }



}
