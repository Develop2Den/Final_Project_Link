package com.finalProject.linkedin.dto.responce.profile;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * description
 *
 * @author Alexander Isai on 10.10.2024.
 */
public record CreateProfileResp(
        @NotNull(message = "Should be not empty")
        Long profileId,
        @NotNull(message = "Should be not empty")
        Long userId,
        String name,
        String surname,
        LocalDateTime birthdate,
        String status,
        String headerPhotoUrl,
        String position,
        String address,
        LocalDateTime createAt
){
}
