package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.profile.CreateProfileReq;
import com.finalProject.linkedin.dto.responce.profile.CreateProfileResp;
import com.finalProject.linkedin.dto.responce.user.CreateUserRes;
import com.finalProject.linkedin.service.serviceIR.ProfileService;
import com.finalProject.linkedin.service.serviceIR.UserService;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * description
 *
 * @author Alexander Isai on 10.10.2024.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/profiles")
@Slf4j
@Validated
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping(value = "")
    @Operation(summary = "Create new profile", description = "Creates a new profile")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<CreateProfileResp> create(@RequestBody @Valid CreateProfileReq createProfileReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfile(createProfileReq));
    }
}
