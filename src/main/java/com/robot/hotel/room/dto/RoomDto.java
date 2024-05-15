package com.robot.hotel.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Dto for Room")
public record RoomDto (Long id, String number, BigDecimal price, int maxCountOfGuests, String roomType){
}