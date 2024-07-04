package com.robot.hotel.room.searchcriteria;

import com.robot.hotel.room.Room;
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

        if(searchParameters != null && searchParameters.types().length > 0){
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
 }
