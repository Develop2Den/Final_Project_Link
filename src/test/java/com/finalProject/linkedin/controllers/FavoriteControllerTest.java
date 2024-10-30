package com.finalProject.linkedin.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

class FavoriteControllerTest {

    @InjectMocks
    private FavoriteController favoriteController;

    @Mock
    private FavoriteServiceImpl favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toggleFavoriteShouldReturnFavoriteResponse() {
        // Arrange
        CreateFavoriteReq req = new CreateFavoriteReq(); // заполните необходимые поля
        CreateFavoriteRes res = new CreateFavoriteRes(); // заполните необходимые поля
        when(favoriteService.addFavorite(any(CreateFavoriteReq.class))).thenReturn(res);

        // Act
        ResponseEntity<CreateFavoriteRes> response = favoriteController.toggleFavorite(req);

        // Assert
        verify(favoriteService).addFavorite(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(res, response.getBody());
    }

    @Test
    void removeFavoriteShouldReturnNoContent() {
        // Arrange
        Long userId = 1L;
        Long targetId = 2L;
        TargetType targetType = TargetType.PROFILE_LIKE; // Убедитесь, что это правильный тип

        // Act
        ResponseEntity<Void> response = favoriteController.removeFavorite(userId, targetId, targetType);

        // Assert
        verify(favoriteService).removeFavorite(userId, targetId, targetType);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getFavoritesByTargetShouldReturnFavoritesList() {
        // Arrange
        Long targetId = 1L;
        TargetType targetType = TargetType.POST_LIKE; // Убедитесь, что это правильный тип
        Favorite favorite = new Favorite(); // заполните необходимые поля
        when(favoriteService.getFavoritesByTarget(targetId, targetType)).thenReturn(Arrays.asList(favorite));

        // Act
        ResponseEntity<List<Favorite>> response = favoriteController.getFavoritesByTarget(targetId, targetType);

        // Assert
        verify(favoriteService).getFavoritesByTarget(targetId, targetType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(favorite), response.getBody());
    }
}

