package com.robot.hotel.room;

import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.room.dto.RoomRequest;
import com.robot.hotel.roomtype.RoomType;
import org.springframework.stereotype.Service;

@Service
public class RoomMapper {
    public RoomDto buildRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .number(room.getNumber())
                .price(room.getPrice())
                .maxCountOfGuests(room.getMaxCountOfGuests())
                .roomType(room.getRoomType().getType())
                .build();
    }

    public Room buildRoomFromRequest(RoomRequest roomRequest, RoomType roomType) {
        return Room.builder()
                .number(roomRequest.getNumber().toLowerCase().strip())
                .price(roomRequest.getPrice())
                .maxCountOfGuests(roomRequest.getMaxCountOfGuests())
                .roomType(roomType)
                .build();
    }
}
