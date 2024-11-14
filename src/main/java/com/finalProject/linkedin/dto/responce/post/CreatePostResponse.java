package com.finalProject.linkedin.dto.responce.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePostResponse(
        @NotNull(message = "Should be not empty")
        Long postId,
        @NotNull(message = "Should be not empty")
        Long authorId,
        @NotNull(message = "Should be not empty")
        String title,
        String photoUrl,
        @NotBlank(message = "Should be not empty")
        String content
) {
}
