package com.robot.hotel.room.search;

import com.robot.hotel.room.dto.RoomSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(RoomSearchParameters searchParameters);
}
