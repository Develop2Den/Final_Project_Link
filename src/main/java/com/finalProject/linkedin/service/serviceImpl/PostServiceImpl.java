package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.post.CreatePostReq;
import com.finalProject.linkedin.dto.responce.post.CreatePostResponse;
import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.PostMapper;
import com.finalProject.linkedin.repository.PostRepository;
import com.finalProject.linkedin.repository.UserRepository;
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
    private final UserRepository userRepository;

    public CreatePostResponse creatPost(CreatePostReq createPostReq) {
        Post post = postMapper.toPost(createPostReq);
        if (post.getPhotoUrl() == null || post.getPhotoUrl().isEmpty()) {
            post.setPhotoUrl("https://img.freepik.com/free-photo/abstract-surface-textures-white-concrete-stone-wall_74190-8189.jpg");
        }
        if(!userRepository.existsById(post.getAuthorId())) {
            throw new NotFoundException("Not found user with id : " + post.getAuthorId());
        }
        post = postRepository.save(post);
        return postMapper.toCreatePostResp(post);
    }

    public CreatePostResponse getPostById(Long postId) {
        Post post = postRepository.findByPostId(postId)
                .filter(x -> x.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("Post not found with id " + postId));

        return postMapper.toCreatePostResp(post);
    }

    public Page<CreatePostResponse> getAllPostsForUser(Long userId ,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        boolean checkedID = postRepository.existsByAuthorId(userId);
        if (checkedID) {
            Page<Post> profilePage = postRepository.findByAuthorIdAndDeletedAtIsNull(userId, pageable);
            return profilePage.map(postMapper::toCreatePostResp);
        }
        else {
            throw new NotFoundException("Profile not found with id " + userId);
        }
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new NotFoundException("Profile not found with id " + postId));

        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public Page<CreatePostResponse> getPostsForRecommends(Long userId ,int page, int size ) {
        Pageable pageable = PageRequest.of(page, size);
        boolean checkedID = postRepository.existsByAuthorId(userId);
        if (checkedID) {
            return (postRepository
                    .findRecommendedPostsBySubscriptions(userId,pageable )
                    .map(postMapper::toCreatePostResp));
        }
        else {
            throw new NotFoundException("Profile not found with id " + userId);
        }
    }
}
