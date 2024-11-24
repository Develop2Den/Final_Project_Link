package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.favorite.CreateFavoriteReq;
import com.finalProject.linkedin.dto.responce.favorite.CreateFavoriteRes;
import com.finalProject.linkedin.entity.Favorite;
import com.finalProject.linkedin.service.serviceImpl.FavoriteServiceImpl;
import com.finalProject.linkedin.utils.enums.TargetType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @PostMapping
    @Operation(summary = "Toggle favorite", description = "Adds or removes a favorite based on the provided request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorite toggled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CreateFavoriteRes> toggleFavorite(@RequestBody @Valid CreateFavoriteReq req) {
        CreateFavoriteRes favoriteRes = favoriteService.addFavorite(req);
        return ResponseEntity.ok(favoriteRes);
    }

    @DeleteMapping
    @Operation(summary = "Remove favorite", description = "Removes a favorite by user ID, target ID, and target type")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Favorite removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Favorite not found")
    })
    public ResponseEntity<Void> removeFavorite(@RequestParam Long userId,
                                               @RequestParam Long targetId,
                                               @RequestParam TargetType targetType) {
        favoriteService.removeFavorite(userId, targetId, targetType);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/target")
    @Operation(summary = "Get favorites by target", description = "Retrieves a list of favorites by target ID and target type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Favorites not found")
    })
    public ResponseEntity<List<Favorite>> getFavoritesByTarget(@RequestParam Long targetId,
                                                               @RequestParam TargetType targetType) {
        List<Favorite> favorites = favoriteService.getFavoritesByTarget(targetId, targetType);
        return ResponseEntity.ok(favorites);
    }

}
