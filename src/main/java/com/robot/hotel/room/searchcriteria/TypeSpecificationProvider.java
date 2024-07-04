package com.robot.hotel.room.searchcriteria;

import com.robot.hotel.room.Room;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TypeSpecificationProvider implements SpecificationProvider<Room> {
    @Override
    public String getKey() {
        return "type";
    }

    @Override
    public Specification<Room> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get("roomType")
                .get("type")
                .in(Arrays.stream(params)
                        .map(String::toLowerCase)
                        .toArray());
    }
}
