package com.robot.hotel.roomtype;

import com.robot.hotel.room.Room;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RoomTypeMapper {
    public RoomTypeDto buildRoomTypeDto(RoomType roomType) {
        return RoomTypeDto.builder()
                .id(roomType.getId())
                .type(roomType.getType())
                .rooms(roomType.getRooms().stream()
                        .map(Room::getNumber)
                        .toList())
                .build();
    }

    public RoomType buildRoomTypeFromRequest(RoomTypeRequest roomTypeRequest) {
        return RoomType.builder()
                .type(roomTypeRequest.getType().toLowerCase())
                .rooms(Collections.EMPTY_LIST)
                .build();
    }
}
