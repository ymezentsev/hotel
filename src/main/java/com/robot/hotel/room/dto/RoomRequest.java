package com.robot.hotel.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for create and update room")
public class RoomRequest {
    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Max size for room number is 20 characters")
    @Schema(description = "Room number", example = "101")
    private String number;

    @Positive(message = "Price must be more than 0")
    @Schema(description = "Room price", example = "1500.00")
    private BigDecimal price;

    @Positive(message = "Maximum count of guests must be more than 0")
    @Schema(description = "Maximum count of guests in room", example = "3")
    private int maxCountOfGuests;

    @NotBlank(message = "Room type is required")
    @Size(max = 20, message = "Max size for room type is 20 characters")
    @Schema(description = "Room type", example = "Standard")
    private String roomType;
}