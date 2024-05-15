package com.robot.hotel.roomtype;

import com.robot.hotel.config.MapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RoomTypeMapper {
    RoomTypeDto toDto(RoomType roomType);
}