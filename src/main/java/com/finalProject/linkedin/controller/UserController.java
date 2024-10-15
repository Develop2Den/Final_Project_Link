package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.user.UserReq;
import com.finalProject.linkedin.dto.responce.user.UserRes;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<UserRes> createUser(@Valid @RequestBody UserReq userRequest) {
        try {
            UserRes userResponse = userService.createUser(userRequest);
            log.info("User created successfully: {}", userResponse);
            return ResponseEntity.ok(userResponse);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<UserRes> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        UserRes userResponse = userService.getCurrentUser(email);
        if (userResponse != null) {
            log.info("Current user retrieved: {}", userResponse);
            return ResponseEntity.ok(userResponse);
        } else {
            log.warn("Current user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
