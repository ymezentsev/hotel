package com.robot.hotel.user;

import com.robot.hotel.config.MapperConfig;
import com.robot.hotel.reservation.ReservationMapper;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.passport.PassportMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = {PassportMapper.class, ReservationMapper.class})
public interface UserMapper {

    @Mappings({
            @Mapping(target = "phoneNumber",
                    expression = "java(user.getCountry().getPhoneCode() + user.getPhoneNumber())")
    })
    UserDto toDto(User user);
}