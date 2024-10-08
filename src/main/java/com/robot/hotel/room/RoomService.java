package com.robot.hotel.room;

import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.room.dto.RoomRequest;
import com.robot.hotel.room.dto.RoomSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {

    Page<RoomDto> findAll(Pageable pageable);

    RoomDto save(RoomRequest roomRequest);

    RoomDto findById(Long id);

    RoomDto findByNumber(String number);

    Page<RoomDto> search(RoomSearchParameters params, Pageable pageable);

    List<RoomDto> findFreeRooms(FreeRoomRequest freeRoomRequest);

    Page<RoomDto> findFreeRoomsPage(FreeRoomRequest freeRoomRequest, Pageable pageable);

    void update(Long id, RoomRequest roomRequest);

    void deleteById(Long id);
}