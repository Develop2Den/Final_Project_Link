package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.dto.responce.subscription.ShortProfileResponse;
import com.finalProject.linkedin.entity.Profile;
import com.finalProject.linkedin.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.followerId = :followerId AND s.followingId = :followingId")
    Optional<Subscription> findByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);


    @Query("SELECT new com.finalProject.linkedin.dto.responce.subscription.ShortProfileResponse(p.name, p.surname, p.headerPhotoUrl) " +
            "FROM Subscription s " +
            "JOIN Profile p ON s.followerId = p.userId " +
            "WHERE s.followingId = :whoGetSubscribedId")
    Page<ShortProfileResponse> findAllSubscribers(@Param("whoGetSubscribedId") Long whoGetSubscribedId, Pageable pageable);

    @Query("SELECT new com.finalProject.linkedin.dto.responce.subscription.ShortProfileResponse(p.name, p.surname, p.headerPhotoUrl) " +
            "FROM Subscription s " +
            "JOIN User u ON u.userId = s.followingId " +
            "JOIN Profile p ON p.userId = u.userId " +
            "WHERE s.followerId = :followerId AND s.deletedAt IS NULL")
    Page<ShortProfileResponse> findAllSubscriptions(@Param("followerId") Long followerId, Pageable pageable);


    Optional<Long> countByFollowingId(Long userId);

    Optional<Long> countByFollowerId(Long userId);

}
