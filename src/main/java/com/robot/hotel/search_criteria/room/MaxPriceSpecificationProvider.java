package com.robot.hotel.search_criteria.room;

import com.robot.hotel.room.Room;
import com.robot.hotel.search_criteria.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MaxPriceSpecificationProvider implements SpecificationProvider<Room> {
    @Override
    public String getKey() {
        return "maxPrice";
    }

    @Override
    public Specification<Room> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), params[0]);
    }
}
