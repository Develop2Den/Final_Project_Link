package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.favorite.CreateFavoriteReq;
import com.finalProject.linkedin.dto.responce.favorite.CreateFavoriteRes;
import com.finalProject.linkedin.entity.Favorite;
import com.finalProject.linkedin.service.serviceImpl.FavoriteServiceImpl;
import com.finalProject.linkedin.utils.enums.TargetType;
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

    private FavoriteServiceImpl favoriteService;

    public FavoriteController(FavoriteServiceImpl favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<CreateFavoriteRes> toggleFavorite(@RequestBody @Valid CreateFavoriteReq req) {
        CreateFavoriteRes favoriteRes = favoriteService.addFavorite(req);
        return ResponseEntity.ok(favoriteRes);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long userId,
                                               @RequestParam Long targetId,
                                               @RequestParam TargetType targetType) {
        favoriteService.removeFavorite(userId, targetId, targetType);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/target")
    public ResponseEntity<List<Favorite>> getFavoritesByTarget(@RequestParam Long targetId,
                                                               @RequestParam TargetType targetType) {
        List<Favorite> favorites = favoriteService.getFavoritesByTarget(targetId, targetType);
        return ResponseEntity.ok(favorites);
    }

}
