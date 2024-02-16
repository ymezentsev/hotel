package com.robot.hotel;

import com.robot.hotel.room.Room;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.roomtype.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;

@Component
public class DBInitializer {
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    RoomRepository roomRepository;

    public void populateDB() {
        roomRepository.deleteAll();
        roomTypeRepository.deleteAll();
        populateRoomTypeTable();
        populateRoomTable();
    }

    private void populateRoomTypeTable() {
        roomTypeRepository.save(new RoomType(null, "lux", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "standart single", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "standart double", Collections.emptyList()));
        roomTypeRepository.save(new RoomType(null, "king", Collections.emptyList()));
    }

    private void populateRoomTable() {
        roomRepository.save(Room.builder()
                .number("101")
                .price(BigDecimal.valueOf(5000))
                .maxCountOfGuests(4)
                .roomType(roomTypeRepository.findByType("lux").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("201")
                .price(BigDecimal.valueOf(1500))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart single").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("202")
                .price(BigDecimal.valueOf(1500))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart single").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("203")
                .price(BigDecimal.valueOf(1000))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart double").orElseThrow())
                .build());

        roomRepository.save(Room.builder()
                .number("204")
                .price(BigDecimal.valueOf(1000))
                .maxCountOfGuests(2)
                .roomType(roomTypeRepository.findByType("standart double").orElseThrow())
                .build());
    }
}
