package com.Final_Project.Linkedin.dto.responce;

import com.nimbusds.openid.connect.sdk.assurance.evidences.attachment.Content;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostResponse (
    @NotNull(message = "Should be not empty")
    Long Author_Id ,
    @NotBlank(message = "Should be not empty")
    String Content
    ){}
