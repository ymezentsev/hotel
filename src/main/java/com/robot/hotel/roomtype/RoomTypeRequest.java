package com.robot.hotel.roomtype;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for create and update room type")
public class RoomTypeRequest {
    @NotBlank(message = "Room type is required")
    @Size(max = 20, message = "Max size for room type is 20 characters")
    private String type;
}
