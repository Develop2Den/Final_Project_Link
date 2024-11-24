package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.service.serviceIR.ReadPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/readPost")
@RequiredArgsConstructor
@Validated
public class ReadPostController {

    private final ReadPostService readPostService;

    @PostMapping("")
    @Operation(description = "Mark post as read")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> subscribed(@RequestParam Long userId, @RequestParam Long postId) {
        readPostService.saveReadPost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}
