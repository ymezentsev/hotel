package com.robot.hotel.search_criteria.user;

import com.robot.hotel.search_criteria.SpecificationProvider;
import com.robot.hotel.user.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class LastnameSpecificationProvider implements SpecificationProvider<User> {
    @Override
    public String getKey() {
        return "lastname";
    }

    @Override
    public Specification<User> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = Arrays.stream(params)
                    .map(param -> criteriaBuilder.like(root.get("lastName"),
                            "%" + param.toLowerCase().strip() + "%"))
                    .toList();
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
