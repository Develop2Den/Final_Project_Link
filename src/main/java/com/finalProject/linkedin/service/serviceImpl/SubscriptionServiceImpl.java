package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.subscription.CreateSubscriptionReq;
import com.finalProject.linkedin.dto.responce.subscription.ShortProfileResponse;
import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.entity.Profile;
import com.finalProject.linkedin.entity.Subscription;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.SubscriptionsMapper;
import com.finalProject.linkedin.repository.SubscriptionRepository;
import com.finalProject.linkedin.service.serviceIR.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionsMapper subscriptionsMapper;

    @Override
    public void subscribed(CreateSubscriptionReq createSubscriptionReq) {
        Subscription subscription = subscriptionsMapper.toSubscription(createSubscriptionReq);

        if (subscription.getFollowerId().equals(subscription.getFollowingId())) {
            throw new IllegalArgumentException("User cannot subscribe to themselves.");
        }

        Optional<Subscription> existingSubscription = subscriptionRepository
                .findByFollowerIdAndFollowingId(subscription.getFollowerId(), subscription.getFollowingId());

        if (existingSubscription.isPresent()) {
            Subscription foundSubscription = existingSubscription.get();
            foundSubscription.setDeletedAt(null);
            subscriptionRepository.save(foundSubscription);
        } else {
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void unsubscribed(CreateSubscriptionReq createSubscriptionReq) {
        Subscription subscription = subscriptionsMapper.toSubscription(createSubscriptionReq);

        Optional<Subscription> existingSubscription = subscriptionRepository
                .findByFollowerIdAndFollowingId(subscription.getFollowerId(), subscription.getFollowingId());

        if (existingSubscription.isPresent()) {
            Subscription foundSubscription = existingSubscription.get();
            foundSubscription.setDeletedAt(LocalDateTime.now());
            subscriptionRepository.save(foundSubscription);
        } else {
            throw new NotFoundException("Subscription not found.");
        }
    } // TODO: NEED TO OPTIMIZE

    @Override
    public Page<ShortProfileResponse> getAllSubscribers(Long whoGetSubscribedId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subscriptionRepository.findAllSubscribers(whoGetSubscribedId , pageable);
    }

    @Override
    public Page<ShortProfileResponse> getAllSubscriptions(Long followerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subscriptionRepository.findAllSubscriptions(followerId , pageable);
    }

    @Override
    public Long getFollowersCount(Long userId) {

       return subscriptionRepository.countByFollowingId(userId)
               .orElseThrow(() -> new NotFoundException("No subscription found , error" + userId));
    }

    @Override
    public Long getFollowingCount(Long userId) {

        return subscriptionRepository.countByFollowerId(userId)
                .orElseThrow(() -> new NotFoundException("No subscription found , error" + userId));
    }
}
