package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.subscription.CreateSubscriptionReq;
import com.finalProject.linkedin.dto.responce.subscription.ShortProfileResponse;
import com.finalProject.linkedin.service.serviceIR.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
@Validated
public class SubscriptionController {

    private final SubscriptionService subscriptionServiceImpl;

    @PostMapping(value = "subscribed")
    @Operation(description = "Subscribed on someone")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> subscribed (@RequestBody @Valid CreateSubscriptionReq createSubscriptionReq){
        System.out.println(createSubscriptionReq);
        subscriptionServiceImpl.subscribed(createSubscriptionReq);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successful");
    }

    @PostMapping(value = "unsubscribed")
    @Operation(description = "Unsubscribed on someone")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> unsubscribed (@RequestBody @Valid CreateSubscriptionReq createSubscriptionReq){
        subscriptionServiceImpl.unsubscribed(createSubscriptionReq);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successful");
    }

    //NEED DTO OR SERVICE METHOD WHERE I CAN GET NAME SURNAME PHOTO OF USER
    @GetMapping(value = "getAllSubscribers/{whoGetSubscribedId}")
    @Operation(summary = "Get profiles of subscribers ",
               description = "Get name , surname , photo of you subscribers with pagination by userId")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Page<ShortProfileResponse>> getAllSubscribers (
            @PathVariable Long whoGetSubscribedId,
            @RequestParam int page,
            @RequestParam int size){

       return ResponseEntity.ok(subscriptionServiceImpl.getAllSubscribers(whoGetSubscribedId, page, size));
    }

    //NEED DTO OR SERVICE METHOD WHERE I CAN GET NAME SURNAME PHOTO OF USER
    @GetMapping(value = "getAllSubscriptions/{followerId}")
    @Operation(summary = "Get your Subscriptions profiles",
            description = "Get name , surname , photo of you subscriptions with pagination by userId")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Page<ShortProfileResponse>> getAllSubscriptions (
            @PathVariable Long followerId,
            @RequestParam int page,
            @RequestParam int size){

       return ResponseEntity.ok(subscriptionServiceImpl.getAllSubscriptions(followerId,page,size));
    }

    @GetMapping(value = "getFollowers/{userId}")
    @Operation(description = "Get the number of subscribers")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Long> getFollowersCount (@PathVariable Long userId){
        return ResponseEntity.ok(subscriptionServiceImpl.getFollowersCount(userId));
    }

    @GetMapping(value = "getFollowing/{userId}")
    @Operation(description = "Get the number of subscriptions")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Long> getFollowingCount (@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionServiceImpl.getFollowingCount(userId));
    }
}
