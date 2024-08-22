package com.robot.hotel.user.mapper;

import com.robot.hotel.config.MapperConfig;
import com.robot.hotel.reservation.ReservationMapper;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = {PassportMapper.class, ReservationMapper.class, RoleMapper.class})
public interface UserMapper {

    @Mappings({
            @Mapping(target = "phoneNumber",
                    expression = "java(user.getCountry().getPhoneCode() + user.getPhoneNumber())"),
            @Mapping(target = "isEnabled", source = "enabled")
    })
    UserDto toDto(User user);
}