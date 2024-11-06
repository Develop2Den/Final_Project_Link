package com.finalProject.linkedin.service.serviceIR;


import com.finalProject.linkedin.dto.request.post.CreatePostReq;
import com.finalProject.linkedin.dto.responce.post.CreatePostResponse;
import org.springframework.data.domain.Page;

public interface PostService {
    CreatePostResponse creatPost(CreatePostReq createPostReq);

    CreatePostResponse getPostById(Long postId);

    void deletePost(Long postId);

    Page<CreatePostResponse> getAllPostsForUser(Long useId ,int page, int size);

    Page<CreatePostResponse> getPostsForRecommends(Long User_id);
}
