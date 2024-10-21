package com.finalProject.linkedin.dto.request.post;

import jakarta.validation.constraints.NotNull;

public record CreatePostReq(
        @NotNull(message = "Should be not empty")
        Long Author_Id,
        @NotNull(message = "Should be noy empty")
        String Title,
        @NotNull(message = "Should be not empty")
        String Content
) {
}
