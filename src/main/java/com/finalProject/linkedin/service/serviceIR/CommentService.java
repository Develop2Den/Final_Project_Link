package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.request.comment.CreateCommentReq;
import com.finalProject.linkedin.dto.responce.comment.CreateCommentRes;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CreateCommentRes create(CreateCommentReq createCommentReq);

    boolean deleteById(Long id);

    List<CreateCommentRes> findAll(Long postId, Pageable pageable);

    long countByPostId(Long postId);
}
