package com.robot.hotel.room.dto;

import java.math.BigDecimal;

public record RoomSearchParameters(String[] types,
                                   BigDecimal minPrice,
                                   BigDecimal maxPrice,
                                   Integer guestsCount) {
}
