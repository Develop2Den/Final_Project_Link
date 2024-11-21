package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.user.CreateUserReq;
import com.finalProject.linkedin.dto.responce.user.CreateUserRes;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create new user", description = "Creates a new user profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<CreateUserRes> createUser(@Valid @RequestBody CreateUserReq createUserRequest) {
        try {
            CreateUserRes createUserResponse = userService.createUser(createUserRequest);
            log.info("User created successfully: {}", createUserResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    @Operation(summary = "Get current user", description = "Fetches the profile of the currently authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Current user not found")
    })
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
    @Operation(summary = "Get user by ID", description = "Fetches user profile by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<CreateUserRes> getUser(@PathVariable long id) {
        log.info("Retrieving User with ID {}", id);
        CreateUserRes userResponse = userService.getUser(id);
        if (userResponse != null) {
            log.info("User retrieved: {}", userResponse);
            return ResponseEntity.ok(userResponse);
        } else {
            log.warn("User with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
