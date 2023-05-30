package com.robot.hotel.service;

import com.robot.hotel.domain.Room;
import com.robot.hotel.domain.Reservation;
import com.robot.hotel.dto.ReservationDto;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.repository.GuestRepository;
import com.robot.hotel.repository.ReservationRepository;
import com.robot.hotel.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static com.robot.hotel.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ReservationService.class)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private RoomRepository roomRepository;
    @MockBean
    private GuestRepository guestRepository;
    @MockBean
    private RoomService roomService;


    @Test
    void shouldFindAll() {
        when(reservationRepository.findAll()).thenReturn(List.of(getReservation1()));
        assertEquals(1, reservationService.findAll().size());
    }

    @Test
    void shouldFindById() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getReservation1()));
        assertThat(reservationService.findById(RESERVATION_ID1)).isPresent();
    }

    @Test
    void shouldFindReservationsByGuestsId() {
        when(reservationRepository.findReservationsByGuestsId(anyLong())).thenReturn(List.of(getReservation1()));
        assertEquals(1, reservationService.findReservationsByGuestsId(GUEST_ID1).size());
    }

    @Test
    void shouldFindReservationsByRoom() {
        when(reservationRepository.findReservationsByRoomId(anyLong())).thenReturn(List.of(getReservation1()));
        when(roomRepository.findRoomsByNumber(anyString())).thenReturn(Optional.ofNullable(getRoom1()));
        assertEquals(1, reservationService.findReservationsByRoom(ROOM_NUMBER1).size());
    }

    @Test
    void shouldFindReservationsByRoomWithException() {
        when(reservationRepository.findReservationsByRoomId(anyLong())).thenReturn(List.of(getReservation1()));
        assertThrows(NoSuchElementException.class, () -> reservationService.findReservationsByRoom(ROOM_NUMBER1));
    }

    @Test
    void shouldSave() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(getReservation1());
        when(roomRepository.findRoomsByNumber(anyString())).thenReturn(Optional.ofNullable(getRoom1()));

        ReservationDto newReservationsDto = getReservationDto1();
        newReservationsDto.setGuests(Set.of(GUEST_ID1.toString()));

        when(guestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getGuest1()));
        when(roomService.findAvailableRooms(anyString(), anyString())).thenReturn(Set.of(getRoomDto1()));
        when(roomService.buildRoomsDto(any(Room.class))).thenReturn(getRoomDto1());

        assertEquals(getReservation1(), reservationService.save(newReservationsDto));
    }

    @Test
    void shouldSaveWithWrongDatesException() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(getReservation1());
        when(roomRepository.findRoomsByNumber(anyString())).thenReturn(Optional.ofNullable(getRoom1()));

        ReservationDto newReservationsDto = getReservationDto1();
        newReservationsDto.setGuests(Set.of(GUEST_ID1.toString()));

        when(guestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getGuest1()));

        assertThrows(WrongDatesException.class, () -> reservationService.save(newReservationsDto));
    }

    @Test
    void shouldSaveWithNoSuchElementException() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(getReservation1());
        assertThrows(NoSuchElementException.class, () -> reservationService.save(getReservationDto1()));
    }

    @Test
    void shouldSaveWithGuestsQuantityException() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(getReservation1());
        when(roomRepository.findRoomsByNumber(anyString())).thenReturn(Optional.ofNullable(getRoom1()));
        assertThrows(GuestsQuantityException.class, () -> reservationService.save(getReservationDto1()));
    }

    @Test
    void shouldFindCurrentReservations() {
        when(reservationRepository.findCurrentReservations(LocalDate.now())).thenReturn(List.of(getReservation1()));
        assertEquals(1, reservationService.findCurrentReservations().size());
    }
}