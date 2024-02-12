package com.robot.hotel.service;

import com.robot.hotel.roomtype.RoomTypeEntity;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.roomtype.RoomTypeRepository;
import com.robot.hotel.roomtype.RoomTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.robot.hotel.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RoomTypeService.class)
class RoomEntityTypeEntityServiceTest {

    @Autowired
    private RoomTypeService roomTypeService;
    @MockBean
    private RoomTypeRepository roomTypeRepository;

    @Test
    void shouldSave() {
        when(roomTypeRepository.save(any(RoomTypeEntity.class))).thenReturn(getRoomType1());
        assertEquals(getRoomType1(), roomTypeService.save(getRoomTypeDto1()));
    }

    @Test
    void shouldSaveWithException() {
        when(roomTypeRepository.save(any(RoomTypeEntity.class))).thenReturn(getRoomType1());
        when(roomTypeRepository.findRoomTypeByType(anyString())).thenReturn(Optional.ofNullable(getRoomType1()));
        assertThrows(DuplicateObjectException.class, () -> roomTypeService.save(getRoomTypeDto1()));
    }

    @Test
    void shouldFindAll() {
        when(roomTypeRepository.findAll()).thenReturn(List.of(getRoomType1(), getRoomType2()));
        assertEquals(2, roomTypeService.findAll().size());
    }

    @Test
    void shouldFindByType() {
        when(roomTypeRepository.findRoomTypeByType(anyString())).thenReturn(Optional.ofNullable(getRoomType1()));
        assertThat(roomTypeService.findByType(ROOM_TYPE_TYPE1)).isPresent();
    }

    @Test
    void shouldFindById() {
        when(roomTypeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getRoomType1()));
        assertThat(roomTypeService.findById(ROOM_TYPE_ID1)).isPresent();
    }
}