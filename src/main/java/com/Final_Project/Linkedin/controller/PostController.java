package com.Final_Project.Linkedin.controller;

import com.Final_Project.Linkedin.dto.request.CreatePostReq;
import com.Final_Project.Linkedin.dto.request.CreateProfileReq;
import com.Final_Project.Linkedin.dto.request.ReadPostReq;
import com.Final_Project.Linkedin.dto.responce.CreatePostResponse;
import com.Final_Project.Linkedin.dto.responce.CreateProfileResp;
import com.Final_Project.Linkedin.service.serviceIR.PostService;
import com.Final_Project.Linkedin.service.serviceImpl.PostServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/home")
@Slf4j
@Validated
public class PostController {
    private final PostServiceImpl postServiceImp;

    @PostMapping(value = "creatPost")
    @Operation(summary = "Create new Post", description = "Creates a new Post")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<CreatePostResponse> createPost(CreatePostReq createPostReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postServiceImp.createPost(createPostReq));
    }

    @DeleteMapping(value = "deletePost")
    @Operation(summary = "Delete Post", description = "Deletes a post based on the provided data")
    @ApiResponse(responseCode = "200", description = "Post deleted successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<?> deletePost(Integer postId) {
        boolean isDeleted = postServiceImp.deletePost(postId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    }

    @GetMapping(value = "getUserPost")
    @Operation(summary = "Get Post", description = "Get a post")
    @ApiResponse(responseCode = "200", description = "Post deleted successfully")
    @ApiResponse(responseCode = "404", description = "Post not found")
    public ResponseEntity<ReadPostReq> readPost(Integer postId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postServiceImp.readPost(postId));
    }

}
