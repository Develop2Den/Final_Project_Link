package com.finalProject.linkedin.controllers;

import com.finalProject.linkedin.controller.FavoriteController;
import com.finalProject.linkedin.dto.request.favorite.CreateFavoriteReq;
import com.finalProject.linkedin.dto.responce.favorite.CreateFavoriteRes;
import com.finalProject.linkedin.entity.Favorite;
import com.finalProject.linkedin.service.serviceImpl.FavoriteServiceImpl;
import com.finalProject.linkedin.utils.enums.TargetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FavoriteControllerTest {

    @Mock
    private FavoriteServiceImpl favoriteService;

    @InjectMocks
    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toggleFavorite_ValidRequest_ReturnsFavoriteRes() {
        CreateFavoriteReq req = new CreateFavoriteReq(1L, 2L, TargetType.PROFILE_LIKE, true);
        CreateFavoriteRes res = new CreateFavoriteRes(req);

        when(favoriteService.addFavorite(any(CreateFavoriteReq.class))).thenReturn(res);

        ResponseEntity<CreateFavoriteRes> response = favoriteController.toggleFavorite(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(res, response.getBody());
        verify(favoriteService).addFavorite(any(CreateFavoriteReq.class));
    }

    @Test
    void removeFavorite_ValidRequest_ReturnsNoContent() {
        Long userId = 1L;
        Long targetId = 2L;
        TargetType targetType = TargetType.PROFILE_LIKE;

        doNothing().when(favoriteService).removeFavorite(userId, targetId, targetType);

        ResponseEntity<Void> response = favoriteController.removeFavorite(userId, targetId, targetType);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(favoriteService).removeFavorite(userId, targetId, targetType);
    }

    @Test
    void getFavoritesByTarget_ValidRequest_ReturnsFavoritesList() {
        Long targetId = 2L;
        TargetType targetType = TargetType.POST_LIKE;
        List<Favorite> favorites = List.of(new Favorite(1L, 1L, 2L, TargetType.POST_LIKE, true));

        when(favoriteService.getFavoritesByTarget(targetId, targetType)).thenReturn(favorites);

        ResponseEntity<List<Favorite>> response = favoriteController.getFavoritesByTarget(targetId, targetType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(favorites, response.getBody());
        verify(favoriteService).getFavoritesByTarget(targetId, targetType);
    }



}
