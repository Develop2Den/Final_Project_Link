package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommentService {

    Comment create(Comment comment);

    boolean deleteById(Long id);

    Page<Comment> findAllByPost(Long postId, Pageable pageable);

    long countByPostId(Long postId);

    Page<Comment> findAll(Pageable pageable);
}
