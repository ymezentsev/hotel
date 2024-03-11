package com.robot.hotel.roomtype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for RoomType")
public class RoomTypeDto {
    private Long id;

    private String type;

    private List<String> rooms;
}
