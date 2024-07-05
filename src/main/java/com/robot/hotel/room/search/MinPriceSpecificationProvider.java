package com.robot.hotel.room.search;

import com.robot.hotel.room.Room;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MinPriceSpecificationProvider implements SpecificationProvider<Room> {
    @Override
    public String getKey() {
        return "minPrice";
    }

    @Override
    public Specification<Room> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), params[0]);
    }
}
