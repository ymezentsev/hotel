package com.robot.hotel.room.dto;

import com.robot.hotel.roomtype.dto.RoomTypeDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dto for Room")
public record RoomDto (Long id, String number, BigDecimal price, int maxCountOfGuests, RoomTypeDto roomType){
}