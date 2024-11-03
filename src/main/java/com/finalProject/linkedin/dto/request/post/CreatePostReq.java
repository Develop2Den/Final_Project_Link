package com.finalProject.linkedin.dto.request.post;

import jakarta.validation.constraints.NotNull;

public record CreatePostReq(
        @NotNull(message = "Should be not empty")
        Long author_id,
        @NotNull(message = "Should be noy empty")
        String title,
        @NotNull(message = "Should be not empty")
        String content
) {
}
