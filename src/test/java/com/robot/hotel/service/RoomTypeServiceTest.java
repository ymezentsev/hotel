package com.robot.hotel.service;

import com.robot.hotel.domain.RoomType;
import com.robot.hotel.domain.Rooms;
import com.robot.hotel.dto.RoomTypeDto;
import com.robot.hotel.repository.RoomTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RoomTypeService.class)
class RoomTypeServiceTest {

    @Autowired
    private RoomTypeService roomTypeService;
    @MockBean
    private RoomTypeRepository roomTypeRepository;

    @Test
    void save() {
    }

    @Test
    void findAll() {
        when(roomTypeRepository.findAll()).thenReturn(List.of(new RoomType(1L, "Standard", List.of(new Rooms()))));
        assertEquals(List.of(new RoomTypeDto(), new RoomTypeDto()), roomTypeService.findAll());
    }

    @Test
    void findByType() {
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}