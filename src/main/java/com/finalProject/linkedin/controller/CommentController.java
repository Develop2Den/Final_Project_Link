package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.comment.CreateCommentReq;
import com.finalProject.linkedin.dto.responce.comment.CreateCommentRes;
import com.finalProject.linkedin.service.serviceIR.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/list")
    public List<CreateCommentRes> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request chats: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return commentService.findAll(pageable);
    }


    @PostMapping("/create")
    public ResponseEntity<CreateCommentRes> createChat(@Valid @RequestBody CreateCommentReq createCommentReq) {
        return ResponseEntity.ok(commentService.create(createCommentReq));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable long id) {
        if (commentService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

}
