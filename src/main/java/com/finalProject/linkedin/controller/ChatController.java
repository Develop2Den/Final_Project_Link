package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.chat.ChatReq;
import com.finalProject.linkedin.dto.responce.chat.ChatResp;
import com.finalProject.linkedin.mapper.ChatMapper;
import com.finalProject.linkedin.service.serviceIR.ChatService;
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
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMapper chatMapper;
    private final ChatService chatService;


    @Operation(summary = " Show method not for production-  Get paginated chats", description = "Get list of pages with pagination")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list")
    public ResponseEntity<List<ChatResp>> getAllChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request chats: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(chatService.findAll(pageable).map(chatMapper::toChatResp).toList());
    }

    @Operation(summary = "Create new chat", description = "Creates a new chat")
    @ApiResponse(responseCode = "201")
    @PostMapping("/create")
    public ResponseEntity<ChatResp> createChat(@Valid @RequestBody ChatReq chatReq) {
        return ResponseEntity.ok(chatMapper.toChatResp(chatService.create(chatMapper.toChat(chatReq))));
    }


    @Operation(summary = "Mark chat and all connected messages as deleted",
            description = "Mark chat and messages as logically deleted by setting 'deletedAt'")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable long id) {
        if (chatService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get chat by ID", description = "Get chat by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ChatResp> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(chatMapper.toChatResp(chatService.getOne(id)));
    }


}
