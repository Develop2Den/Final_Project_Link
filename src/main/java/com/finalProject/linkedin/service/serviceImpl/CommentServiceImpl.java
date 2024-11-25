package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.comment.CreateCommentReq;
import com.finalProject.linkedin.dto.responce.comment.CreateCommentRes;
import com.finalProject.linkedin.entity.Comment;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.CommentMapper;
import com.finalProject.linkedin.repository.CommentRepository;
import com.finalProject.linkedin.service.serviceIR.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public CreateCommentRes create(CreateCommentReq createCommentReq) {
        return commentMapper.toCreateCommentRes(commentRepository.save(commentMapper.toComment(createCommentReq)));
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
    public List<CreateCommentRes> findAll(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdAndDeletedAtIsNull(postId, pageable).map(commentMapper::toCreateCommentRes).toList();
    }

    @Override
    public long countByPostId(Long postId) {
        return commentRepository.countByPostIdAndDeletedAtIsNull(postId);
    }

}
