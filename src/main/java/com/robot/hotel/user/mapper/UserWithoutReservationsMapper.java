package com.robot.hotel.user.mapper;

import com.robot.hotel.config.MapperConfig;
import com.robot.hotel.user.dto.UserDtoWithoutReservation;
import com.robot.hotel.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = {PassportMapper.class})
public interface UserWithoutReservationsMapper {
    @Mappings({
            @Mapping(target = "phoneNumber",
                    expression = "java(user.getCountry().getPhoneCode() + user.getPhoneNumber())")
    })
    UserDtoWithoutReservation toDtoWithoutReservations(User user);
}