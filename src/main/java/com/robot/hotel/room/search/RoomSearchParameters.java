package com.robot.hotel.room.search;

import java.math.BigDecimal;

public record RoomSearchParameters(String[] types,
                                   BigDecimal minPrice,
                                   BigDecimal maxPrice,
                                   Integer guestsCount) {
}
