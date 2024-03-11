package com.robot.hotel.reservation;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.WrongDatesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ContainerConfiguration.class)
class ReservationServiceImplTest {
    @Autowired
    ReservationService reservationService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all reservations")
    void findAllTest() {
        assertEquals(4, reservationService.findAll().size());
    }

    @Test
    @DisplayName("Successful find reservation by id")
    void findByIdTest() {
        Long id = testDBUtils.getReservationIdByRoom("203");
        assertEquals(LocalDate.now().plusDays(4), reservationService.findById(id).getCheckInDate());
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
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        assertEquals(2, reservationService.findReservationsByUserId(id).size());
    }

    @Test
    @DisplayName("Find reservations by room number")
    void findReservationsByRoomTest() {
        assertEquals(2, reservationService.findReservationsByRoom("204").size());
    }

    @Test
    @DisplayName("Successful create new reservation")
    void saveTest() {
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                Set.of(user1Id.toString(), user2Id.toString()));

        assertAll(
                () -> assertNotNull(reservationService.save(reservationRequest).getId()),
                () -> assertEquals(5, reservationService.findAll().size())
        );
    }

    @Test
    @DisplayName("Fail create new reservation (throw NoSuchElementException - wrong room)")
    void saveThrowNoSuchElementExceptionWrongRoomTest() {
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("104",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                Set.of(user1Id.toString(), user2Id.toString()));

        assertThrows(NoSuchElementException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw WrongDatesException - wrong dates)")
    void saveThrowWrongDatesExceptionWrongDatesTest() {
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now(), Set.of(user1Id.toString(),
                user2Id.toString()));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw WrongDatesException - too long reservation)")
    void saveThrowWrongDatesExceptionTooLongTest() {
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now().plusDays(61),
                Set.of(user1Id.toString(), user2Id.toString()));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw WrongDatesException - too early reservation)")
    void saveThrowWrongDatesExceptionTooEarlyTest() {
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now().plusDays(181),
                LocalDate.now().plusDays(182),
                Set.of(user1Id.toString(), user2Id.toString()));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Fail create new reservation (throw GuestsQuantityException - too many guests)")
    void saveThrowGuestsQuantityExceptionTest() {
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");
        Long user3Id = testDBUtils.getUserIdByEmail("dmitr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("201",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                Set.of(user1Id.toString(), user2Id.toString(), user3Id.toString()));

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
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("101",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                Set.of(user1Id.toString(), user2Id.toString()));

        assertThrows(WrongDatesException.class,
                () -> reservationService.save(reservationRequest));
    }

    @Test
    @DisplayName("Find all current reservations")
    void findCurrentReservationsTest() {
        assertEquals(3, reservationService.findCurrentReservations().size());
    }

    @Test
    @DisplayName("Find all current reservations for a specific room")
    void findCurrentReservationsForSpecificRoomTest() {
        assertEquals(1, reservationService.findCurrentReservationsForSpecificRoom("204").size());
    }

    @Test
    @DisplayName("Successful delete reservation")
    void deleteByIdTest() {
        Long id = testDBUtils.getReservationIdByRoom("101");
        reservationService.deleteById(id);
        assertEquals(3, reservationService.findAll().size());
    }

    @Test
    @DisplayName("Fail delete reservation")
    void deleteByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> reservationService.deleteById(200L));
    }
}