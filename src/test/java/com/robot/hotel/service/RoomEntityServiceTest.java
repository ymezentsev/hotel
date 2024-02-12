package com.robot.hotel.service;

import com.robot.hotel.room.RoomEntity;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.roomtype.RoomTypeRepository;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.room.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.robot.hotel.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RoomService.class)
class RoomEntityServiceTest {

    @Autowired
    private RoomService roomService;
    @MockBean
    private RoomRepository roomRepository;
    @MockBean
    private RoomTypeRepository roomTypeRepository;
    @MockBean
    private ReservationRepository reservationRepository;

    @Test
    void shouldFindAll() {
        when(roomRepository.findAll()).thenReturn(List.of(getRoom1(), getRoom2()));
        assertEquals(2, roomService.findAll().size());
    }

    @Test
    void shouldSave() {
        when(roomRepository.save(any(RoomEntity.class))).thenReturn(getRoom1());
        when(roomTypeRepository.findRoomTypeByType(anyString())).thenReturn(Optional.ofNullable(getRoomType1()));
        assertEquals(getRoom1(), roomService.save(getRoomDto1()));
    }

    @Test
    void shouldSaveWithExceptionNoSuchElement() {
        when(roomRepository.save(any(RoomEntity.class))).thenReturn(getRoom1());
        assertThrows(NoSuchElementException.class, () -> roomService.save(getRoomDto1()));
    }

    @Test
    void shouldSaveWithExceptionDuplicateObject() {
        when(roomRepository.save(any(RoomEntity.class))).thenReturn(getRoom1());
        when(roomRepository.findRoomsByNumber(anyString())).thenReturn(Optional.ofNullable(getRoom1()));
        assertThrows(DuplicateObjectException.class, () -> roomService.save(getRoomDto1()));
    }

    @Test
    void shouldFindById() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getRoom1()));
        assertThat(roomService.findById(ROOM_ID1)).isPresent();
    }

    @Test
    void shouldFindRoomsByNumber() {
        when(roomRepository.findRoomsByNumber(anyString())).thenReturn(Optional.ofNullable(getRoom1()));
        assertThat(roomService.findRoomsByNumber(ROOM_NUMBER1)).isPresent();
    }

    @Test
    void shouldFindByType() {
        when(roomTypeRepository.findRoomTypeByType(anyString())).thenReturn(Optional.ofNullable(getRoomType1()));
        when(roomRepository.findRoomsByRoomTypeId(anyLong())).thenReturn(List.of(getRoom1()));
        assertEquals(1, roomService.findByType(ROOM_TYPE_TYPE1).size());
    }

    @Test
    void shouldFindByTypeWithException() {
        when(roomRepository.findRoomsByRoomTypeId(anyLong())).thenReturn(List.of(getRoom1()));
        assertThrows(NoSuchElementException.class, () -> roomService.findByType(ROOM_TYPE_TYPE1));
    }

    @Test
    void shouldFindByPriceMoreThanOrEqual() {
        when(roomRepository.findRoomsByPriceMoreThanOrEqual(any(BigDecimal.class))).thenReturn(List.of(getRoom1()));
        assertEquals(1, roomService.findByPriceMoreThanOrEqual(ROOM_PRICE).size());
    }

    @Test
    void shouldFindByPriceLessThanOrEqual() {
        when(roomRepository.findRoomsByPriceLessThanOrEqual(any(BigDecimal.class))).thenReturn(List.of(getRoom1()));
        assertEquals(1, roomService.findByPriceLessThanOrEqual(ROOM_PRICE).size());
    }

    @Test
    void findByGuestsCount() {
        when(roomRepository.findRoomsByGuestsCount(anyInt())).thenReturn(List.of(getRoom1()));
        assertEquals(1, roomService.findByGuestsCount(ROOM_MAX_COUNT_OF_GUESTS).size());
    }
}