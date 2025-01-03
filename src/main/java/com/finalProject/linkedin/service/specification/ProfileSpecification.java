package com.finalProject.linkedin.service.specification;

import com.finalProject.linkedin.entity.Profile;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * description
 *
 * @author Alexander Isai on 10.10.2024.
 */
public class ProfileSpecification {

    public static Specification<Profile> hasEmail(String email) {
        return (Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                (email == null || email.isBlank())
                        ? builder.conjunction()
                        : builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<Profile> hasName(String name) {
        return (Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                (name == null || name.isBlank())
                        ? builder.conjunction()
                        : builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Profile> hasSurname(String surname) {
        return (Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                (surname == null || surname.isBlank())
                        ? builder.conjunction()
                        : builder.like(root.get("surname"), "%" + surname + "%");
    }

    public static Specification<Profile> isNotDeleted() {
        return (Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.isNull(root.get("deletedAt"));
    }
}
