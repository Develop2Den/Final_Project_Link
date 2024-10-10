package com.Final_Project.Linkedin.controllers;

import com.Final_Project.Linkedin.dto.user.userReq.UserReq;
import com.Final_Project.Linkedin.dto.user.userRes.UserRes;
import com.Final_Project.Linkedin.services.userService.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    public ResponseEntity<UserRes> createUser(@Valid @RequestBody UserReq userRequest) {
        try {
            UserRes userResponse = userFacade.createUser(userRequest);
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
        UserRes userResponse = userFacade.getCurrentUser(email);
        if (userResponse != null) {
            log.info("Current user retrieved: {}", userResponse);
            return ResponseEntity.ok(userResponse);
        } else {
            log.warn("Current user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
