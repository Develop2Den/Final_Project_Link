package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.post.CreatePostReq;
import com.finalProject.linkedin.dto.responce.post.CreatePostResponse;
import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.entity.Profile;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.PostMapper;
import com.finalProject.linkedin.repository.PostRepository;
import com.finalProject.linkedin.service.serviceIR.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public CreatePostResponse creatPost(CreatePostReq createPostReq) {
        Post post = postMapper.toPost(createPostReq);

        post = postRepository.save(post);

        return postMapper.toCreatePostResp(post);
    }

    public CreatePostResponse getPostById(Long postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException("Profile not found with id " + postId));

        return postMapper.toCreatePostResp(post);
    }

    public Page<CreatePostResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> profilePage = postRepository.findAll(pageable);
        return profilePage.map(postMapper::toCreatePostResp);
    }

    public Page<CreatePostResponse> getAllPostsForUser(Long useId ,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> profilePage = postRepository.findByAuthorId(useId , pageable);
        return profilePage.map(postMapper::toCreatePostResp);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException("Profile not found with id " + postId));

        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }
    //Page<CreatePostResponse> getPostsForRecommends(Long User_id);
}
