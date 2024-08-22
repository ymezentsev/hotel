package com.robot.hotel.user.mapper;

import com.robot.hotel.config.MapperConfig;
import com.robot.hotel.user.dto.RoleDto;
import com.robot.hotel.user.model.Role;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {
    RoleDto toDto(Role role);
}