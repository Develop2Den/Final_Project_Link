package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.entity.Comment;
import com.finalProject.linkedin.entity.Notification;
import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.repository.CommentRepository;
import com.finalProject.linkedin.repository.NotificationRepository;
import com.finalProject.linkedin.repository.PostRepository;
import com.finalProject.linkedin.service.serviceIR.CommentService;
import com.finalProject.linkedin.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;

    @Override
    public Comment create(Comment comment) {
        createNotification(comment);
        return commentRepository.save(comment);
    }

    private void createNotification(Comment comment) {
        Post post = postRepository.findByPostId(comment.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found with id " + comment.getPostId()));
        notificationRepository.save(new Notification(
                comment.getPostId(),
                comment.getAuthorId(),
                (comment.getContent().length() > 20) ? comment.getContent().substring(0, 20) : comment.getContent(),
                NotificationType.COMMENT,
                post.getAuthorId()
        ));
    }

    @Override
    public boolean deleteById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found with id " + id));
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return true;
    }


    @Override
    public Page<Comment> findAllByPost(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdAndDeletedAtIsNullOrderByCreatedAtDesc(postId, pageable);
    }

    @Override
    public long countByPostId(Long postId) {
        return commentRepository.countByPostIdAndDeletedAtIsNullOrderByCreatedAtDesc(postId);
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

}
