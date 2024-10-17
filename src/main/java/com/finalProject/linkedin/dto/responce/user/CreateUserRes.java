package com.finalProject.linkedin.dto.responce.user;

import lombok.Data;

@Data
public class CreateUserRes {

    private Long id;
    private String email;
    private Boolean isVerified;

}
