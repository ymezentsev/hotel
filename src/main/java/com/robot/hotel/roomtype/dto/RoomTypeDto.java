package com.robot.hotel.roomtype.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto for RoomType")
public record RoomTypeDto(Long id, String type) {
}