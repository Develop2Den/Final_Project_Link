package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.favorite.CreateFavoriteReq;
import com.finalProject.linkedin.dto.responce.favorite.CreateFavoriteRes;
import com.finalProject.linkedin.entity.Favorite;
import com.finalProject.linkedin.repository.FavoriteRepository;
import com.finalProject.linkedin.service.serviceIR.FavoriteService;
import com.finalProject.linkedin.utils.enums.TargetType;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public CreateFavoriteRes addFavorite(CreateFavoriteReq req) {
        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndTargetIdAndTargetType(req.getUserId(), req.getTargetId(), req.getTargetType()).stream().findFirst();

        Favorite favorite;
        if (existingFavorite.isPresent()) {
            favorite = existingFavorite.get();
            favorite.setIsPositive(req.getIsPositive());
        } else {
            favorite = new Favorite();
            favorite.setUserId(req.getUserId());
            favorite.setTargetId(req.getTargetId());
            favorite.setTargetType(req.getTargetType());
            favorite.setIsPositive(req.getIsPositive());
        }
        favoriteRepository.save(favorite);
        return new CreateFavoriteRes(favorite);
    }

    public void removeFavorite(Long userId, Long targetId, TargetType targetType) {
        favoriteRepository.deleteAll(favoriteRepository.findByUserIdAndTargetIdAndTargetType(userId, targetId, targetType));
    }

    public List<Favorite> getFavoritesByTarget(Long targetId, TargetType targetType) {
        return favoriteRepository.findByTargetIdAndTargetType(targetId, targetType);
    }
}

