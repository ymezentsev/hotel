package com.robot.hotel.room;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoomServiceImplTest {
    @Autowired
    RoomService roomService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all rooms")
    void findAllTest() {
        assertEquals(5, roomService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Successful create new room")
    void saveTest() {
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");
        assertAll(
                () -> assertNotNull(roomService.save(roomRequest).id()),
                () -> assertEquals(6, roomService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Fail create new room (throw DuplicateObjectException)")
    void saveThrowDuplicateObjectExceptionTest() {
        RoomRequest roomRequest = new RoomRequest("101", BigDecimal.valueOf(1000), 3, "lux");
        assertThrows(DuplicateObjectException.class,
                () -> roomService.save(roomRequest));
    }

    @Test
    @DisplayName("Fail create new room (throw NoSuchElementException)")
    void saveThrowNoSuchElementExceptionTest() {
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "new lux");
        assertThrows(NoSuchElementException.class,
                () -> roomService.save(roomRequest));
    }

    @Test
    @DisplayName("Successful find room by id")
    void findByIdTest() {
        Long id = testDBUtils.getRoomIdByNumber("101");
        assertEquals(4, roomService.findById(id).maxCountOfGuests());
    }

    @Test
    @DisplayName("Fail find room by id")
    void findByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomService.findById(200L));
    }

    @Test
    @DisplayName("Successful find room by number")
    void findByNumberTest() {
        assertEquals(4, roomService.findByNumber("101").maxCountOfGuests());
    }

    @Test
    @DisplayName("Fail find room by number")
    void findByNumberThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomService.findByNumber("1100"));
    }

    @Test
    @DisplayName("Successful find rooms by types")
    void findByTypeTest() {
        assertEquals(2, roomService
                .findByType("standart single", Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Fail find rooms by types")
    void findByTypeThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomService.findByType("new lux", Pageable.unpaged()));
    }

    @Test
    @DisplayName("Find rooms with price more or equal than given")
    void findByPriceMoreThanOrEqualTest() {
        assertAll(
                () -> assertEquals(1, roomService
                        .findByPriceMoreThanOrEqual(BigDecimal.valueOf(5000), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(3, roomService
                        .findByPriceMoreThanOrEqual(BigDecimal.valueOf(1500), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(0, roomService
                        .findByPriceMoreThanOrEqual(BigDecimal.valueOf(5500), Pageable.unpaged())
                        .getTotalElements())
        );
    }

    @Test
    @DisplayName("Find rooms with price less or equal than given")
    void findByPriceLessThanOrEqualTest() {
        assertAll(
                () -> assertEquals(4, roomService
                        .findByPriceLessThanOrEqual(BigDecimal.valueOf(1500), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(2, roomService
                        .findByPriceLessThanOrEqual(BigDecimal.valueOf(1000), Pageable.unpaged())
                        .getTotalElements()),
                () -> assertEquals(0, roomService
                        .findByPriceLessThanOrEqual(BigDecimal.valueOf(500), Pageable.unpaged())
                        .getTotalElements())
        );
    }

    @Test
    @DisplayName("Find rooms with max count of guests more or equal than given")
    void findByGuestsCountTest() {
        assertAll(
                () -> assertEquals(0, roomService
                        .findByGuestsCount(5, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(1, roomService
                        .findByGuestsCount(3, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(5, roomService
                        .findByGuestsCount(2, Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Successful find free rooms")
    void findFreeRoomsTest() {
        FreeRoomRequest freeRoomRequest = new FreeRoomRequest(LocalDate.now(), LocalDate.now().plusDays(3));

        assertEquals(3, roomService.findFreeRooms(freeRoomRequest).size());
    }

    @Test
    @DisplayName("Fail find free rooms")
    void findFreeRoomsThrowWrongDatesExceptionTest() {
        FreeRoomRequest freeRoomRequest = new FreeRoomRequest(LocalDate.now(), LocalDate.now());

        assertThrows(WrongDatesException.class,
                () -> roomService.findFreeRooms(freeRoomRequest));
    }

    @Test
    @DisplayName("Successful update room")
    void updateTest() {
        Long id = testDBUtils.getRoomIdByNumber("101");
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");

        roomService.update(id, roomRequest);
        assertAll(
                () -> assertEquals("105", roomService.findById(id).number()),
                () -> assertEquals(5, roomService.findAll(Pageable.unpaged()).getTotalElements())
        );
   }

    @Test
    @DisplayName("Fail update room (throw NoSuchElementException, wrong id)")
    void updateThrowNoSuchElementExceptionWrongIdTest() {
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");

        assertThrows(NoSuchElementException.class,
                () -> roomService.update(200L, roomRequest));
    }

    @Test
    @DisplayName("Fail update room (throw NoSuchElementException, wrong type)")
    void updateThrowNoSuchElementExceptionWrongITypeTest() {
        Long id = testDBUtils.getRoomIdByNumber("101");
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "new lux");

        assertThrows(NoSuchElementException.class,
                () -> roomService.update(id, roomRequest));
    }

    @Test
    @DisplayName("Fail update room (throw DuplicateObjectException)")
    void updateThrowDuplicateObjectExceptionTest() {
        Long id = testDBUtils.getRoomIdByNumber("101");
        RoomRequest roomRequest = new RoomRequest("201", BigDecimal.valueOf(1000), 3, "lux");

        assertThrows(DuplicateObjectException.class,
                () -> roomService.update(id, roomRequest));
    }

    @Test
    @DisplayName("Successful delete room")
    void deleteByIdTest() {
        Long id = testDBUtils.getRoomIdByNumber("201");
        roomService.deleteById(id);
        assertEquals(4, roomService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Fail delete room (throw NoSuchElementException)")
    void deleteByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomService.deleteById(200L));
    }

    @Test
    @DisplayName("Fail delete room (throw NotEmptyObjectException)")
    void deleteByIdThrowNotEmptyObjectExceptionTest() {
        Long id = testDBUtils.getRoomIdByNumber("204");
        assertThrows(NotEmptyObjectException.class,
                () -> roomService.deleteById(id));
    }
}