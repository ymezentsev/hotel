package com.robot.hotel.roomtype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDto {
    private Long id;
    private String type;
    private List<String> rooms;
}
