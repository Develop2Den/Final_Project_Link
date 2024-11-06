package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.request.favorite.CreateFavoriteReq;
import com.finalProject.linkedin.dto.responce.favorite.CreateFavoriteRes;
import com.finalProject.linkedin.entity.Favorite;
import com.finalProject.linkedin.utils.enums.TargetType;
import java.util.List;

public interface FavoriteService {

    CreateFavoriteRes addFavorite(CreateFavoriteReq req);

    void removeFavorite(Long userId, Long targetId, TargetType targetType);

    List<Favorite> getFavoritesByTarget(Long targetId, TargetType targetType);
}
