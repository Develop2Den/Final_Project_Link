package com.finalProject.linkedin.dto.request.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePostReq(
        @NotNull(message = "Should be not empty")
        Long authorId,
        @NotNull(message = "Should be noy empty")
        String title,
        @Pattern(
                regexp = "^$|^(https?|ftp)://[^\\s]+\\.(jpg|jpeg|png|gif|bmp)$",
                message = "Must be either empty or contain exactly one valid URL pointing to an image"
        )
        String photoUrl,
        @NotNull(message = "Should be not empty")
        String content
) {
}
