package com.robot.hotel.roomtype;

import com.robot.hotel.roomtype.dto.RoomTypeDto;
import com.robot.hotel.roomtype.dto.RoomTypeRequest;

import java.util.List;

public interface RoomTypeService {
    RoomTypeDto save(RoomTypeRequest roomTypeRequest);

    List<RoomTypeDto> findAll();

    RoomTypeDto findByType(String type);

    RoomTypeDto findById(Long id);

    void update(Long id, RoomTypeRequest roomTypeRequest);

    void deleteById(Long id);
}