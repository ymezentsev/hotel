package com.robot.hotel.room;

import com.robot.hotel.DBInitializer;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RoomServiceTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    private DBInitializer dbInitializer;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all rooms")
    void findAllTest() {
        assertEquals(5, roomService.findAll().size());
    }

    @Test
    @DisplayName("Successful create new room")
    void saveTest() {
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");
        assertAll(
                () -> assertNotNull(roomService.save(roomRequest).getId()),
                () -> assertEquals(6, roomService.findAll().size())
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
        Long id = getIdByNumber("101");
        assertEquals(4, roomService.findById(id).getMaxCountOfGuests());
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
        assertEquals(4, roomService.findByNumber("101").getMaxCountOfGuests());
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
        assertEquals(2, roomService.findByType("standart single").size());
    }

    @Test
    @DisplayName("Fail find rooms by types")
    void findByTypeThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomService.findByType("new lux"));
    }

    @Test
    @DisplayName("Find rooms with price more or equal than given")
    void findByPriceMoreThanOrEqualTest() {
        assertAll(
                () -> assertEquals(1, roomService.findByPriceMoreThanOrEqual(BigDecimal.valueOf(5000)).size()),
                () -> assertEquals(3, roomService.findByPriceMoreThanOrEqual(BigDecimal.valueOf(1500)).size()),
                () -> assertEquals(0, roomService.findByPriceMoreThanOrEqual(BigDecimal.valueOf(5500)).size())
        );
    }

    @Test
    @DisplayName("Find rooms with price less or equal than given")
    void findByPriceLessThanOrEqualTest() {
        assertAll(
                () -> assertEquals(4, roomService.findByPriceLessThanOrEqual(BigDecimal.valueOf(1500)).size()),
                () -> assertEquals(2, roomService.findByPriceLessThanOrEqual(BigDecimal.valueOf(1000)).size()),
                () -> assertEquals(0, roomService.findByPriceLessThanOrEqual(BigDecimal.valueOf(500)).size())
        );
    }

    @Test
    @DisplayName("Find rooms with max count of guests more or equal than given")
    void findByGuestsCountTest() {
        assertAll(
                () -> assertEquals(0, roomService.findByGuestsCount(5).size()),
                () -> assertEquals(1, roomService.findByGuestsCount(3).size()),
                () -> assertEquals(5, roomService.findByGuestsCount(2).size())
        );
    }

    @Test
    @DisplayName("Find free rooms")
    void findFreeRoomsTest() {
    }

    @Test
    @DisplayName("Successful update room")
    void updateTest() {
        Long id = getIdByNumber("101");
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");

        roomService.update(id, roomRequest);
        assertAll(
                () -> assertEquals("105", roomService.findById(id).getNumber()),
                () -> assertEquals(5, roomService.findAll().size())
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
        Long id = getIdByNumber("101");
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "new lux");

        assertThrows(NoSuchElementException.class,
                () -> roomService.update(id, roomRequest));
    }

    @Test
    @DisplayName("Fail update room (throw DuplicateObjectException)")
    void updateThrowDuplicateObjectExceptionTest() {
        Long id = getIdByNumber("101");
        RoomRequest roomRequest = new RoomRequest("201", BigDecimal.valueOf(1000), 3, "lux");

        assertThrows(DuplicateObjectException.class,
                () -> roomService.update(id, roomRequest));
    }

    @Test
    @DisplayName("Successful delete room")
    void deleteByIdTest() {
        Long id = getIdByNumber("101");
        roomService.deleteById(id);
        assertEquals(4, roomService.findAll().size());
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
        Long id = getIdByNumber("204");
        assertThrows(NotEmptyObjectException.class,
                () -> roomService.deleteById(id));
    }

    private Long getIdByNumber(String number) {
        return roomRepository.findByNumber(number)
                .orElseThrow()
                .getId();
    }
}