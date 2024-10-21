package com.finalProject.linkedin.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentReq {
    private Long commentId;
    @NotNull(message = "post id field must be filled ")
    private Long postId;
    @NotNull(message = "author id field must be filled")
    private Long authorId;
    @NotBlank(message = " content can't be empty")
    private String content;

}
