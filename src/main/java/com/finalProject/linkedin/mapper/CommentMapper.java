package com.finalProject.linkedin.mapper;

import com.finalProject.linkedin.dto.request.comment.CreateCommentReq;
import com.finalProject.linkedin.dto.responce.comment.CreateCommentRes;
import com.finalProject.linkedin.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Comment toComment(CreateCommentReq createCommentReq);

    CreateCommentRes toCreateCommentRes(Comment comment);
}
