package com.robot.hotel.room.searchcriteria;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(RoomSearchParameters searchParameters);
}
