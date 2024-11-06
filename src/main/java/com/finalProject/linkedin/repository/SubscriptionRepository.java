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

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.followerId = :followerId AND s.followingId = :followingId")
    Optional<Subscription> findByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);


    @Query("SELECT p " +
            "FROM Subscription s " +
            "JOIN Profile p ON s.followerId = p.userId " +
            "WHERE s.followingId = :whoGetSubscribedId AND p.deletedAt IS NULL")
    Page<Profile> findAllSubscribers(@Param("whoGetSubscribedId") Long whoGetSubscribedId, Pageable pageable);

    @Query("SELECT p " +
            "FROM Subscription s " +
            "JOIN Profile p ON s.followingId = p.userId " +
            "WHERE s.followerId = :followerId AND p.deletedAt IS NULL")
    Page<Profile> findAllSubscriptions(@Param("followerId") Long followerId, Pageable pageable);

    Optional<Long> countByFollowingId(Long userId);

    Optional<Long> countByFollowerId(Long userId);

}
