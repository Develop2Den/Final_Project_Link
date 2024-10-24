package com.finalProject.linkedin.dto.responce.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostResponse(
        @NotNull(message = "Should be not empty")
        Long Author_Id,
        @NotNull(message = "Should be not empty")
        String Title,
        @NotBlank(message = "Should be not empty")
        String Content
) {
}
