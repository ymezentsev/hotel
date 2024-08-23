package com.robot.hotel.reservation;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBUtils;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.reservation.dto.ReservationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class ReservationServiceImplTest {
    @Autowired
    ReservationService reservationService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    DBUtils DBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all reservations")
    void findAllTest() {
        assertEquals(4, reservationService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Successful find reservation by id")
    void findByIdTest() {
        Long id = DBUtils.getReservationIdByRoom("203");
        assertEquals(LocalDate.now().plusDays(4), reservationService.findById(id).checkInDate());
    }

    @Test
    @DisplayName("Fail find reservation by id")
    void findByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> reservationService.findById(200L));
    }

    @Test
    @DisplayName("Find reservations by user id")
    void findReservationsByUserIdTest() {
        Long id = DBUtils.getUserIdByEmail("sidor@gmail.com");
        assertEquals(2, reservationService.findReservationsByUserId(id, Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Find reservations by room number")
    void findReservationsByRoomTest() {
        assertEquals(2,
                reservationService.findReservationsByRoom("204", Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Successful create new reservation")
    void saveTest() {
        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                Set.of("sidor@gmail.com", "sidor_andr@gmail.com"));

        assertAll(
                () -> assertNotNull(reservationService.save(reservationRequest).id()),
                () -> assertEquals(5, reservationService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Fail create new reservation (throw NoSuchElementException - wrong room)")
    void saveThrowNoSuchElementExceptionWrongRoomTest() {
        ReservationRequest reservationRequest = new ReservationRequest("104",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                Set.of("sidor@gmail.com", "sidor_andr@gmail.com"));

        assertThrows(NoSuchElementException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw WrongDatesException - wrong dates)")
    void saveThrowWrongDatesExceptionWrongDatesTest() {
        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now(),
                Set.of("sidor@gmail.com", "sidor_andr@gmail.com"));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw WrongDatesException - too long reservation)")
    void saveThrowWrongDatesExceptionTooLongTest() {
        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now().plusDays(61),
                Set.of("sidor@gmail.com", "sidor_andr@gmail.com"));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw WrongDatesException - too early reservation)")
    void saveThrowWrongDatesExceptionTooEarlyTest() {
        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now().plusDays(181),
                LocalDate.now().plusDays(182),
                Set.of("sidor@gmail.com", "sidor_andr@gmail.com"));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw GuestsQuantityException - too many guests)")
    void saveThrowGuestsQuantityExceptionTest() {
        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                Set.of("sidor@gmail.com", "sidor_andr@gmail.com", "dmitr@gmail.com"));

        assertThrows(GuestsQuantityException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw NoSuchElementException - wrong user")
    void saveThrowNoSuchElementExceptionWrongGuestTest() {
        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                Set.of("200"));

        assertThrows(NoSuchElementException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw WrongDatesException - room occupied)")
    void saveThrowWrongDatesExceptionRoomOccupiedTest() {
        ReservationRequest reservationRequest = new ReservationRequest("101",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                Set.of("sidor@gmail.com", "sidor_andr@gmail.com"));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Find all current reservations")
    void findCurrentReservationsTest() {
        assertEquals(3, reservationService.findCurrentReservations(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Find all current reservations for a specific room")
    void findCurrentReservationsForSpecificRoomTest() {
        assertEquals(1,
                reservationService.findCurrentReservationsForSpecificRoom("204",
                        Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Successful delete reservation")
    void deleteByIdTest() {
        Long id = DBUtils.getReservationIdByRoom("101");
        reservationService.deleteById(id);
        assertEquals(3, reservationService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Fail delete reservation")
    void deleteByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> reservationService.deleteById(200L));
    }
}