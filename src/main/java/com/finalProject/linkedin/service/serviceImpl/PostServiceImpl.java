package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.post.CreatePostReq;
import com.finalProject.linkedin.dto.responce.post.CreatePostResponse;
import com.finalProject.linkedin.entity.Notification;
import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.PostMapper;
import com.finalProject.linkedin.repository.NotificationRepository;
import com.finalProject.linkedin.repository.PostRepository;
import com.finalProject.linkedin.repository.SubscriptionRepository;
import com.finalProject.linkedin.repository.UserRepository;
import com.finalProject.linkedin.service.serviceIR.PostService;
import com.finalProject.linkedin.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;

    public CreatePostResponse creatPost(CreatePostReq createPostReq) {
        Post post = postMapper.toPost(createPostReq);
        if (post.getPhotoUrl() == null || post.getPhotoUrl().isEmpty()) {
            post.setPhotoUrl("");
        }
        if (!userRepository.existsById(post.getAuthorId())) {
            throw new NotFoundException("Not found user with id : " + post.getAuthorId());
        }
        post = postRepository.save(post);
        createNotificationForSubscribers(post);
        return postMapper.toCreatePostResp(post);
    }

    public void createNotificationForSubscribers(Post post) {
        List<Notification> notificationList = subscriptionRepository.findByFollowingId(post.getAuthorId())
                .stream()
                .map(sub -> new Notification(
                        post.getPostId(),
                        post.getAuthorId(),
                        (post.getContent().length() > 20) ? post.getContent().substring(0, 20) : post.getContent(),
                        NotificationType.POST,
                        sub.getFollowerId()
                ))
                .toList();
        notificationRepository.saveAll(notificationList);
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
