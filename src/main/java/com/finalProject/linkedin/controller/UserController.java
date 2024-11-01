package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.user.CreateUserReq;
import com.finalProject.linkedin.dto.responce.user.CreateUserRes;
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
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<CreateUserRes> createUser(@Valid @RequestBody CreateUserReq createUserRequest) {
        try {
            CreateUserRes createUserResponse = userService.createUser(createUserRequest);
            log.info("User created successfully: {}", createUserResponse);
            return ResponseEntity.ok(createUserResponse);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<CreateUserRes> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        CreateUserRes createUserResponse = userService.getCurrentUser(email);
        if (createUserResponse != null) {
            log.info("Current user retrieved: {}", createUserResponse);
            return ResponseEntity.ok(createUserResponse);
        } else {
            log.warn("Current user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateUserRes> getUser(@PathVariable long id) {
        log.info("Retrieving User with ID {}", id);
        CreateUserRes customerResponse = userService.getUser(id);
        if (customerResponse != null) {
            log.info("User retrieved: {}", customerResponse);
            return ResponseEntity.ok(customerResponse);
        } else {
            log.warn("User with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
