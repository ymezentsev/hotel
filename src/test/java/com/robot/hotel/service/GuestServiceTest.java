/*
package com.robot.hotel.service;

import com.robot.hotel.guest.Guest;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.guest.GuestService;
import com.robot.hotel.guest.GuestRepository;
import com.robot.hotel.reservation.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.robot.hotel.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GuestService.class)
class GuestServiceTest {

    @Autowired
    private GuestService guestService;
    @MockBean
    private GuestRepository guestRepository;
    @MockBean
    private ReservationRepository reservationRepository;

    @Test
    void shouldFindAll() {
        when(guestRepository.findAll()).thenReturn(List.of(getGuest1()));
        assertEquals(1, guestService.findAll().size());
    }

    @Test
    void shouldFindGuestByReservation() {
        when(guestRepository.findGuestsByReservationsId(anyLong())).thenReturn(Set.of(getGuest1()));
        assertEquals(1, guestService.findGuestByReservation(RESERVATION_ID1).size());
    }

    @Test
    void shouldSave() {
        when(guestRepository.save(any(Guest.class))).thenReturn(getGuest1());
        assertEquals(getGuest1(), guestService.save(getGuestDto1()));
    }

    @Test
    void shouldSaveWithExceptionDuplicateObject() {
        when(guestRepository.save(any(Guest.class))).thenReturn(getGuest1());
        when(guestRepository.findGuestsByTelNumber(anyString())).thenReturn(Optional.ofNullable(getGuest1()));
        assertThrows(DuplicateObjectException.class, () -> guestService.save(getGuestDto1()));
    }

    @Test
    void shouldFindById() {
        when(guestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getGuest1()));
        assertThat(guestService.findById(GUEST_ID1)).isPresent();
    }

    @Test
    void shouldFindByEmail() {
        when(guestRepository.findGuestsByEmail(anyString())).thenReturn(Optional.ofNullable(getGuest1()));
        assertThat(guestService.findByEmail(GUEST_EMAIL)).isPresent();
    }

    @Test
    void shouldFindByTelNumber() {
        when(guestRepository.findGuestsByTelNumber(anyString())).thenReturn(Optional.ofNullable(getGuest1()));
        assertThat(guestService.findByTelNumber(GUEST_TEL_NUMBER)).isPresent();
    }

    @Test
    void shouldFindByPassportSerialNumber() {
        when(guestRepository.findGuestsByPassportSerialNumber(anyString())).thenReturn(Optional.ofNullable(getGuest1()));
        assertThat(guestService.findByPassportSerialNumber(GUEST_PASSPORT_SERIAL_NUMBER)).isPresent();
    }

    @Test
    void shouldFindByLastName() {
        when(guestRepository.findGuestsByLastName(anyString())).thenReturn(List.of(getGuest1()));
        assertEquals(1, guestService.findByLastName(GUEST_LAST_NAME).size());
    }
}*/
