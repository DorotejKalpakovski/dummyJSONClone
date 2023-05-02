package com.example.restapi.specifications;

import com.example.restapi.domain.users.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class UserDefaultFilterSpec implements Specification<User> {
    private final String value;

    public UserDefaultFilterSpec(String value) {
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.or(
                criteriaBuilder.like(root.get("firstName"), value),
                criteriaBuilder.like(root.get("lastName"), value),
                criteriaBuilder.like(root.get("email"), value)
        );
    }
}
