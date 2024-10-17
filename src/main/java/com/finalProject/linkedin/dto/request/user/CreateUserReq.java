package com.finalProject.linkedin.dto.request.user;


import com.finalProject.linkedin.utils.password.PasswordValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserReq {

    @Email(message = "Email should be valid")
    private String email;

    @Valid
    private PasswordValidator password;

    private Boolean isVerified;
}