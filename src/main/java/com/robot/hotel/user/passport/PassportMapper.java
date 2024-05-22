package com.robot.hotel.user.passport;

import com.robot.hotel.config.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class)
public interface PassportMapper {
    @Mappings({
            @Mapping(target = "country", source = "country.countryName")
    })
    PassportDto toDto(Passport passport);
}