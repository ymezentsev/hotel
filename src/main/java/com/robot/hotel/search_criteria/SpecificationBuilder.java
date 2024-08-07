package com.robot.hotel.search_criteria;

import com.robot.hotel.room.dto.RoomSearchParameters;
import com.robot.hotel.user.dto.UserSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(RoomSearchParameters searchParameters);

    Specification<T> build(UserSearchParametersDto searchParameters);
}
