package com.finalProject.linkedin.dto.request.subscription;

import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionReq (
        @NotNull(message = "Should be not empty")
        Long followerId,
        @NotNull(message = "Should be not empty")
        Long followingId
) {
}
