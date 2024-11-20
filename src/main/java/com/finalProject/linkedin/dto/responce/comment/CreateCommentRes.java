package com.finalProject.linkedin.dto.responce.comment;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateCommentRes {
    private Long commentId;
    private Long postId;
    private Long authorId;
    private String content;
    private LocalDateTime createdAt;
}
