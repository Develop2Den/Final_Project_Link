package com.finalProject.linkedin.mapper;

import com.finalProject.linkedin.dto.request.post.CreatePostReq;
import com.finalProject.linkedin.dto.request.subscription.CreateSubscriptionReq;
import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionsMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Subscription toSubscription (CreateSubscriptionReq createSubscriptionReq);
}
