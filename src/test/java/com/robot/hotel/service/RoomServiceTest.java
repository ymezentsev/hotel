package com.robot.hotel.service;

import com.robot.hotel.domain.Room;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.repository.ReservationRepository;
import com.robot.hotel.repository.RoomTypeRepository;
import com.robot.hotel.repository.RoomRepository;
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
class RoomServiceTest {

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
    void shouldSave() throws DublicateObjectException {
        when(roomRepository.save(any(Room.class))).thenReturn(getRoom1());
        when(roomTypeRepository.findRoomTypeByType(anyString())).thenReturn(Optional.ofNullable(getRoomType1()));
        assertEquals(getRoom1(), roomService.save(getRoomDto1()));
    }

    @Test
    void shouldSaveWithExceptionNoSuchElement() throws NoSuchElementException {
        when(roomRepository.save(any(Room.class))).thenReturn(getRoom1());
        assertThrows(NoSuchElementException.class, () -> roomService.save(getRoomDto1()));
    }

    @Test
    void shouldSaveWithExceptionDublicateObject() throws DublicateObjectException {
        when(roomRepository.save(any(Room.class))).thenReturn(getRoom1());
        when(roomRepository.findRoomsByNumber(anyString())).thenReturn(Optional.ofNullable(getRoom1()));
        assertThrows(DublicateObjectException.class, () -> roomService.save(getRoomDto1()));
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
    void shouldFindByPriceMoreThan() {
        when(roomRepository.findRoomsByPriceMoreThan(any(BigDecimal.class))).thenReturn(List.of(getRoom1()));
        assertEquals(1, roomService.findByPriceMoreThan(ROOM_PRICE).size());
    }

    @Test
    void shouldFindByPriceLessThan() {
        when(roomRepository.findRoomsByPriceLessThan(any(BigDecimal.class))).thenReturn(List.of(getRoom1()));
        assertEquals(1, roomService.findByPriceLessThan(ROOM_PRICE).size());
    }

    @Test
    void findByGuestsCount() {
        when(roomRepository.findRoomsByGuestsCount(anyInt())).thenReturn(List.of(getRoom1()));
        assertEquals(1, roomService.findByGuestsCount(ROOM_MAX_COUNT_OF_GUESTS).size());
    }
}