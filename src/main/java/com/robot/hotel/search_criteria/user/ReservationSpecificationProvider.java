package com.robot.hotel.search_criteria.user;

import com.robot.hotel.search_criteria.SpecificationProvider;
import com.robot.hotel.user.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ReservationSpecificationProvider implements SpecificationProvider<User> {
    @Override
    public String getKey() {
        return "reservation";
    }

    @Override
    public Specification<User> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.get("reservations")
                    .get("id")
                    .in(Arrays.stream(params).toArray());
        };
    }
}
