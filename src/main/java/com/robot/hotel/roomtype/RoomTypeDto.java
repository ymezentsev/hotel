package com.robot.hotel.roomtype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Dto for RoomType")
public record RoomTypeDto(Long id, String type) {
}