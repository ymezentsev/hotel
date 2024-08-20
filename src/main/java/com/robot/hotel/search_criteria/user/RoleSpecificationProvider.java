package com.robot.hotel.search_criteria.user;

import com.robot.hotel.search_criteria.SpecificationProvider;
import com.robot.hotel.user.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleSpecificationProvider implements SpecificationProvider<User> {
    @Override
    public String getKey() {
        return "role";
    }

    @Override
    public Specification<User> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get("roles")
                .get("name")
                .in(Arrays.stream(params)
                        .map(name -> name.toUpperCase().strip())
                        .toArray());
    }
}
