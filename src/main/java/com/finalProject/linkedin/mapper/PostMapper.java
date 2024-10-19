package com.finalProject.linkedin.mapper;

import com.finalProject.linkedin.dto.request.post.CreatePostReq;
import com.finalProject.linkedin.dto.responce.post.CreatePostResponse;
import com.finalProject.linkedin.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Post toPost (CreatePostReq createPostReq);

    CreatePostResponse toCreatePostResp (Post post);
}
