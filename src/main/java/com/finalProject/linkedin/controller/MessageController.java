package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.GetMessageWithProfileResp;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import com.finalProject.linkedin.service.serviceIR.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Get paginated messages", description = "Get list of messages with pagination")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list")
    public List<MessageResp> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request chats: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return messageService.findAll(pageable);
    }

    @Operation(summary = "Create new message", description = "Creates a new message")
    @ApiResponse(responseCode = "201")
    @PostMapping("/create")
    public ResponseEntity<MessageResp> createChat(@Valid @RequestBody MessageReq MessageReq) {
        return ResponseEntity.ok(messageService.create(MessageReq));
    }

    @Operation(summary = "Mark message as deleted", description = "Mark message as logically deleted by setting 'deletedAt'")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable long id) {
        if (messageService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get paginated messages between two users", description = "Get list of messages with pagination between two users by id, sorted by created date ")
    @ApiResponse(responseCode = "200")
    @GetMapping("/chat")
    public List<MessageResp> getAllMessagesChat(
            @RequestParam long id1,
            @RequestParam long id2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        Pageable pageable = PageRequest.of(page, size);
        return messageService.getChatMessages(id1, id2, pageable);
    }

    @Operation(summary = "Get paginated messages between two different users", description = "Get list of messages with pagination between user by id and other users. 1 message for each pair  ")
    @ApiResponse(responseCode = "200")
    @GetMapping("/latest")
    public List<GetMessageWithProfileResp> getAllMessagesLatest
            (@RequestParam long id,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageService.findLatestMessagesForUser(id, pageable);
    }

}