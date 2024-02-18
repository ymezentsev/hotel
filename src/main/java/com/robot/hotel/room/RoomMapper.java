package com.robot.hotel.room;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.roomtype.RoomType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Function;

@Service
public class RoomMapper {
    public RoomDto buildRoomsDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .number(room.getNumber())
                .price(room.getPrice())
                .maxCountOfGuests(room.getMaxCountOfGuests())
                .roomType(room.getRoomType().getType())
                .reservations(room.getReservations().stream()
                        .map(getReservationsString())
                        .toList())
                .build();
    }

    public Room buildRoomFromRequest(RoomRequest roomRequest, RoomType roomType) {
        return Room.builder()
                .number(roomRequest.getNumber().toLowerCase())
                .price(roomRequest.getPrice())
                .maxCountOfGuests(roomRequest.getMaxCountOfGuests())
                .roomType(roomType)
                .reservations(Collections.emptyList())
                .build();
    }

    private Function<Reservation, String> getReservationsString() {
        return reservations -> "Id:" + reservations.getId().toString()
                + ", " + reservations.getCheckInDate().toString() + " - "
                + reservations.getCheckOutDate().toString();
    }
}
