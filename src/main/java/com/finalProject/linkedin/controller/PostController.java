package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.post.CreatePostReq;
import com.finalProject.linkedin.dto.responce.post.CreatePostResponse;
import com.finalProject.linkedin.service.serviceImpl.PostServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(postServiceImp.creatPost(createPostReq));
    }

    @DeleteMapping(value = "deletePost")
    @Operation(summary = "Delete Post", description = "Mark profile as logically deleted by setting 'deletedAt'")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> deletePost(Long postId) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "getPostById")
    @Operation(summary = "Get Post", description = "Get posts by post id")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CreatePostResponse> getPostByPostId(Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postServiceImp.getPostById(postId));
    }

    @GetMapping(value = "getAllPostsForUser")
    @Operation(summary = "Get post for User", description = "Get posts with pagination for User by his id")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Page<CreatePostResponse>> getAllPostsForUser (Long useId ,int page, int size){
        return ResponseEntity.ok(postServiceImp.getAllPostsForUser(useId,page,size));
    }

    @GetMapping(value = "getAllPosts") // need to delete ? they need that ?
    @Operation(summary = "Get post from base", description = "Get posts with pagination from base")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Page<CreatePostResponse>> getAllPosts (int page, int size){
        return ResponseEntity.ok(postServiceImp.getAllPosts(page,size));
    }

}
