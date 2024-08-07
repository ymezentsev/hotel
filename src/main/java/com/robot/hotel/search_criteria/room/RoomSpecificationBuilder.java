package com.robot.hotel.search_criteria.room;

import com.robot.hotel.room.Room;
import com.robot.hotel.room.dto.RoomSearchParameters;
import com.robot.hotel.search_criteria.SpecificationBuilder;
import com.robot.hotel.search_criteria.SpecificationProviderManager;
import com.robot.hotel.user.dto.UserSearchParametersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomSpecificationBuilder implements SpecificationBuilder<Room> {
    private final SpecificationProviderManager<Room> specificationProviderManager;
    @Override
    public Specification<Room> build(RoomSearchParameters searchParameters) {
        Specification<Room> spec = Specification.where(null);

        if(searchParameters != null && searchParameters.types() != null && searchParameters.types().length > 0){
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("type")
                    .getSpecification(searchParameters.types()));
        }

        if(searchParameters != null && searchParameters.minPrice() != null){
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("minPrice")
                    .getSpecification(String.valueOf(searchParameters.minPrice()).split(" ")));
        }

        if(searchParameters != null && searchParameters.maxPrice() != null){
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("maxPrice")
                    .getSpecification(String.valueOf(searchParameters.maxPrice()).split(" ")));
        }

        if(searchParameters != null && searchParameters.guestsCount() != null){
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("guestsCount")
                    .getSpecification(String.valueOf(searchParameters.guestsCount()).split(" ")));
        }
        return spec;
    }

    @Override
    public Specification<Room> build(UserSearchParametersDto searchParameters) {
        return null;
    }
}
