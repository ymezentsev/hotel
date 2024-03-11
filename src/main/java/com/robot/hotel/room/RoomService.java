package com.robot.hotel.room;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface RoomService {

    List<RoomDto> findAll();

    RoomDto save(RoomRequest roomRequest);

    RoomDto findById(Long id);

    RoomDto findByNumber(String number);

    List<RoomDto> findByType(String type);

    List<RoomDto> findByPriceMoreThanOrEqual(BigDecimal price);

    List<RoomDto> findByPriceLessThanOrEqual(BigDecimal price);

    List<RoomDto> findByGuestsCount(int guestCount);

    Set<RoomDto> findFreeRooms(FreeRoomRequest freeRoomRequest);

    void update(Long id, RoomRequest roomRequest);

    void deleteById(Long id);
}
