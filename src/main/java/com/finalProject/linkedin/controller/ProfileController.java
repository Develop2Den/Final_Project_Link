package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.profile.CreateProfileReq;
import com.finalProject.linkedin.dto.responce.profile.CreateProfileResp;
import com.finalProject.linkedin.service.serviceIR.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{profileId}")
    @Operation(summary = "Get profile by ID", description = "Get profile by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CreateProfileResp> getProfileById(@PathVariable Long profileId) {
        return ResponseEntity.ok(profileService.getProfileById(profileId));
    }

    @GetMapping(value = "/user")
    @Operation(summary = "Get profile by userID", description = "Get profile by its userID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CreateProfileResp> getProfileByUserId(@RequestParam(value = "userId") Long userId) {
            return ResponseEntity.ok(profileService.getProfileByUserId(userId));
        }


    @GetMapping
    @Operation(summary = "Get paginated profiles", description = "Get list of profiles with pagination")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<CreateProfileResp>> getAllProfiles(
            @RequestParam(defaultValue = "0") @Min(0) @Max(10_000) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer limit,
            @RequestParam(required = false) @Size(max = 100) String email,
            @RequestParam(required = false) @Size(max = 100) String name,
            @RequestParam(required = false) @Size(max = 100) String surname) {
        return ResponseEntity.ok(profileService.getAllProfiles(page, limit, email, name, surname));
    }

    @PutMapping("/{profileId}")
    @Operation(summary = "Update profile", description = "Update profile by its ID")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CreateProfileResp> updateProfile(
            @PathVariable Long profileId,
            @RequestBody @Valid CreateProfileReq createProfileReq) {
        return ResponseEntity.ok(profileService.updateProfile(profileId, createProfileReq));
    }

    @DeleteMapping("/{profileId}")
    @Operation(summary = "Mark profile as deleted", description = "Mark profile as logically deleted by setting 'deletedAt'")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> markProfileAsDeleted(@PathVariable Long profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

}
