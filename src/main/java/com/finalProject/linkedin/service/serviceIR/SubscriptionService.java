package com.finalProject.linkedin.service.serviceIR;



import com.finalProject.linkedin.dto.request.subscription.CreateSubscriptionReq;
import com.finalProject.linkedin.dto.responce.subscription.ShortProfileResponse;
import com.finalProject.linkedin.entity.Profile;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubscriptionService {
    void subscribed (CreateSubscriptionReq createSubscriptionReq);

    void unsubscribed (CreateSubscriptionReq createSubscriptionReq);

    Page<ShortProfileResponse> getAllSubscribers (Long whoGetSubscribedId, int page, int size);

    Page<ShortProfileResponse> getAllSubscriptions (Long followerId,int page, int size);

    Long getFollowersCount (Long userId);
    Long getFollowingCount (Long userId);
}
