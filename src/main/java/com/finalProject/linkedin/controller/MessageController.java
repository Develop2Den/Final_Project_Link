package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import com.finalProject.linkedin.service.serviceIR.MessageService;
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

    @GetMapping("/list")
    public List<MessageResp> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request chats: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return messageService.findAll(pageable);
    }


    @PostMapping("/create")
    public ResponseEntity<MessageResp> createChat(@Valid @RequestBody MessageReq MessageReq) {
        return ResponseEntity.ok(messageService.create(MessageReq));
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<Void> deleteMessage(@PathVariable long id) {
        if (messageService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

}
