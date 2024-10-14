package com.Final_Project.Linkedin.dto.request.PostReq;

import jakarta.validation.constraints.NotNull;

public record CreatePostReq (
    @NotNull(message = "Should be not empty")
    Long Author_Id ,
    @NotNull(message = "Should be not empty")
    String Content
){}
