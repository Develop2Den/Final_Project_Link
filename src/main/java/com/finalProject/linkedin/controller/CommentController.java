package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.comment.CreateCommentReq;
import com.finalProject.linkedin.dto.responce.comment.CreateCommentRes;
import com.finalProject.linkedin.service.serviceIR.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "Get paginated comments by post id", description = "Get list of comments by post id with pagination")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list/{id}")
    public List<CreateCommentRes> getAllCommentsByPostId(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request comments by id: ID = {} page={}, size={}", id, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return commentService.findAll(id, pageable);
    }

    @Operation(summary = "Create new comment", description = "Creates a new comment")
    @ApiResponse(responseCode = "201")
    @PostMapping("/create")
    public ResponseEntity<CreateCommentRes> createComment(@Valid @RequestBody CreateCommentReq createCommentReq) {
        log.info("Request create new  comment : {}", createCommentReq);
        return ResponseEntity.ok(commentService.create(createCommentReq));
    }


    @Operation(summary = "Mark comment as deleted",
            description = "Mark comment as logically deleted by setting 'deletedAt'")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable long id) {
        log.info("Request delete comments  id: ID = {}", id);
        if (commentService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get number of comments for post by post id", description = "Get number of comments for post by post id")
    @ApiResponse(responseCode = "200")
    @GetMapping("/count/{id}")
    public ResponseEntity<Long> getCommentCount(@PathVariable long id) {
        log.info("Request count comments by Post id: ID = {}", id);
        return ResponseEntity.ok(commentService.countByPostId(id));
    }

}
