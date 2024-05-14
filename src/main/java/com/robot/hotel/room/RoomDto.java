package com.robot.hotel.room;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RoomDto (Long id, String number, BigDecimal price, int maxCountOfGuests, String roomType){
}