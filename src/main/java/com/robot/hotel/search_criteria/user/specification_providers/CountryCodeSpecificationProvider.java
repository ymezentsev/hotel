package com.robot.hotel.search_criteria.user.specification_providers;

import com.robot.hotel.search_criteria.SpecificationProvider;
import com.robot.hotel.user.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CountryCodeSpecificationProvider implements SpecificationProvider<User> {
    @Override
    public String getKey() {
        return "countryCode";
    }

    @Override
    public Specification<User> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get("passport")
                .get("country")
                .get("countryCode")
                .in(Arrays.stream(params)
                        .map(name -> name.toUpperCase().strip())
                        .toArray());
    }
}
