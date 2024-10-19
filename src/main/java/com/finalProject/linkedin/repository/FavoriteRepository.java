package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Favorite;
import com.finalProject.linkedin.utils.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);
    List<Favorite> findByTargetIdAndTargetType(Long targetId, TargetType targetType);
}
