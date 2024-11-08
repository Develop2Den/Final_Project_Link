package com.finalProject.linkedin.repository;


import com.finalProject.linkedin.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostId(Long postId);

    boolean existsByAuthorId(Long authorId);

    Page<Post> findByAuthorIdAndDeletedAtIsNull(Long authorId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.authorId IN (SELECT s.followingId FROM Subscription s WHERE s.followerId = :userId AND s.deletedAt IS NULL) " +
            "AND p.deletedAt IS NULL " +
            "AND p.postId NOT IN (SELECT rp.postId FROM ReadPost rp WHERE rp.userId = :userId) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findRecommendedPostsBySubscriptions(@Param("userId") Long userId, Pageable pageable);



}
