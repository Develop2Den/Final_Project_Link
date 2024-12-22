package com.finalProject.linkedin.controller;


import com.finalProject.linkedin.dto.request.notification.NotificationReq;
import com.finalProject.linkedin.dto.responce.notification.NotificationRes;
import com.finalProject.linkedin.mapper.NotificationMapper;
import com.finalProject.linkedin.service.serviceIR.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/notifications")
@RequiredArgsConstructor

public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Operation(summary = "Show method not for production - Get paginated notifications", description = "Get list of notifications with pagination")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list")
    public ResponseEntity<List<NotificationRes>> getAllNotification(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(notificationService.findAll(pageable).map(notificationMapper::toNotificationRes).toList());
    }

    @Operation(summary = "Create new notification", description = "Creates a new notification")
    @ApiResponse(responseCode = "201")
    @PostMapping("/create")
    public ResponseEntity<NotificationRes> createNotification(@Valid @RequestBody NotificationReq notificationReq) {
        return ResponseEntity.ok(notificationMapper.toNotificationRes(notificationService.create(notificationMapper.toNotification(notificationReq))));
    }

    @Operation(summary = "Mark notification as deleted", description = "Mark notification as logically deleted by setting 'deletedAt'")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable long id) {
        if (notificationService.deleteById(id)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get paginated notifications by user id", description = "Get list of notifications with pagination by user id")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list/{id}")
    public ResponseEntity<List<NotificationRes>> getAllNotification(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(notificationService.findByIdAndDeleteFalse(id, pageable).map(notificationMapper::toNotificationRes).toList());
    }

    @Operation(summary = "Get number of unread notifications by user id", description = "Get number of unread notifications by user id sorted by created date")
    @ApiResponse(responseCode = "200")
    @GetMapping("/count/{id}")
    public ResponseEntity<Long> getAllNotificationCount(
            @PathVariable long id
    ) {
        log.info("Request count unread notifications by id: ID = {}", id);
        return ResponseEntity.ok(notificationService.countByRecipientIdReadFalse(id));
    }

    @Operation(summary = "Get paginated unread notifications by user id", description = "Get paginated unread notifications by user id sorted by created date")
    @ApiResponse(responseCode = "200")
    @GetMapping("/unread/{id}")
    public ResponseEntity<List<NotificationRes>> getAllUnreadNotification(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Pageable request unread notification by id: ID = {},  page={}, size={}", id, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(notificationService.findByIdAndReadFalse(id, pageable).map(notificationMapper::toNotificationRes).toList());
    }

    @GetMapping("/{notificationId}")
    @Operation(summary = "Get notification by ID", description = "Get notification by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<NotificationRes> getNotificationById(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationMapper.toNotificationRes(notificationService.getNotificationById(notificationId)));
    }

    @PutMapping("/read/{notificationId}")
    @Operation(summary = "mark notification as read", description = "mark notification as read by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> setNotificationRead(@PathVariable Long notificationId) {
        if (notificationService.readTrue(notificationId)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/read")
    @Operation(summary = "mark notifications as read", description = "mark notifications as read by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> setMessagesRead(@RequestBody List<Long> ids) {
        if (notificationService.readTrue(ids)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }
}
