package com.robot.hotel.room;

import com.robot.hotel.config.MapperConfig;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.roomtype.RoomTypeMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = RoomTypeMapper.class)
public interface RoomMapper {
    RoomDto toDto(Room room);
}