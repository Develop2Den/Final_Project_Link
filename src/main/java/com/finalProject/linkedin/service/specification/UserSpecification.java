package com.finalProject.linkedin.service.specification;

import com.finalProject.linkedin.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasEmail(String email) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                email == null ? null : builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> isNotDeleted() {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.isNull(root.get("deletedAt"));
    }
}
