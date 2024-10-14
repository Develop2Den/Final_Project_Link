package com.Final_Project.Linkedin.service.serviceImpl;

import com.Final_Project.Linkedin.dto.request.CreatePostReq;
import com.Final_Project.Linkedin.dto.request.ReadPostReq;
import com.Final_Project.Linkedin.dto.responce.CreatePostResponse;
import com.Final_Project.Linkedin.entity.Post;
import com.Final_Project.Linkedin.repository.PostRepository;
import com.Final_Project.Linkedin.service.serviceIR.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository ;
    public CreatePostResponse createPost(CreatePostReq createPostReq) {
        //TODO
        return null;
    }
    public ReadPostReq readPost(Integer postId) {
        //TODO
        return null;
    }
    public Boolean deletePost(Integer post_Id) {
        //TODO
        return null;
    }
}
