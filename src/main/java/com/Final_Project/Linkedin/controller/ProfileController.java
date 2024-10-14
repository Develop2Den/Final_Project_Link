package com.Final_Project.Linkedin.controller;

import com.Final_Project.Linkedin.dto.request.CreateProfileReq;
import com.Final_Project.Linkedin.dto.responce.PostResp.CreateProfileResp;
import com.Final_Project.Linkedin.service.serviceIR.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
