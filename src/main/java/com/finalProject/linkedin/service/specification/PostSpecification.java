package com.finalProject.linkedin.service.specification;

import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    //title
    public static Specification<Post> hasTitle(String title ) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                title == null ? null : builder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Post> isNotDeleted() {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.isNull(root.get("deletedAt"));
    }
}
