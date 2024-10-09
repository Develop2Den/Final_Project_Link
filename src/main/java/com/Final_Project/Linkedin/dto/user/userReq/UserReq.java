package com.Final_Project.Linkedin.dto.user.userReq;


import com.Final_Project.Linkedin.password.PasswordValidator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserReq {

    @Email(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$", message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Valid
    private PasswordValidator password;

    private boolean isVerified;
}
