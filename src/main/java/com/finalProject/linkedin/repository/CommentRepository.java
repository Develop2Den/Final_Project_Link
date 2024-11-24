package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countByPostIdAndDeletedAtIsNull(long postId);

    Page<Comment> findByPostIdAndDeletedAtIsNull(long postId, Pageable pageable);

}
