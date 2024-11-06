package com.finalProject.linkedin.mapper;

import com.finalProject.linkedin.dto.request.subscription.CreateSubscriptionReq;
import com.finalProject.linkedin.dto.responce.subscription.ShortProfileResponse;
import com.finalProject.linkedin.entity.Profile;
import com.finalProject.linkedin.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionsMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Subscription toSubscription (CreateSubscriptionReq createSubscriptionReq);

    ShortProfileResponse toShortProfile (Profile profile);
}
