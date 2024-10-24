package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.chat.ChatReq;
import com.finalProject.linkedin.dto.responce.chat.ChatResp;
import com.finalProject.linkedin.service.serviceIR.ChatService;
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

    private final ChatService chatService;

    @GetMapping("/list")
    public List<ChatResp> getAllChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request chats: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return chatService.findAll(pageable);
    }


    @PostMapping("/create")
    public ResponseEntity<ChatResp> createChat(@Valid @RequestBody ChatReq chatReq) {
        return ResponseEntity.ok(chatService.create(chatReq));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable long id) {
        if (chatService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();

    }


}
