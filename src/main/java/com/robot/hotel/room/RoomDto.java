package com.robot.hotel.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;

    private String number;

    private BigDecimal price;

    private int maxCountOfGuests;

    private String roomType;
}
