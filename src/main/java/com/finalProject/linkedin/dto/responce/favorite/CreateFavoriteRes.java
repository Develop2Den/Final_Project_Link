package com.finalProject.linkedin.dto.responce.favorite;

import com.finalProject.linkedin.dto.request.favorite.CreateFavoriteReq;
import com.finalProject.linkedin.entity.Favorite;
import com.finalProject.linkedin.utils.enums.TargetType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateFavoriteRes {
    private Long favoriteId;
    private Long userId;
    private Long targetId;
    private TargetType targetType;
    private Boolean isPositive;
    private LocalDateTime createdAt;

    public CreateFavoriteRes(Favorite favorite) {
        this.favoriteId = favorite.getFavoriteId();
        this.userId = favorite.getUserId();
        this.targetId = favorite.getTargetId();
        this.targetType = favorite.getTargetType();
        this.isPositive = favorite.getIsPositive();
        this.createdAt = favorite.getCreatedAt();
    }

    public CreateFavoriteRes(CreateFavoriteReq req) {
    }
}
