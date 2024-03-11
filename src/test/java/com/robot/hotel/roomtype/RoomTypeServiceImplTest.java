package com.robot.hotel.roomtype;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ContainerConfiguration.class)
class RoomTypeServiceImplTest {
    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Successful create new room type")
    void saveTest() {
        assertAll(
                () -> assertNotNull(roomTypeService.save(new RoomTypeRequest("new lux")).getId()),
                () -> assertEquals(5, roomTypeService.findAll().size())
        );
    }

    @Test
    @DisplayName("Fail create new room type")
    void saveThrowDuplicateObjectExceptionTest() {
        assertThrows(DuplicateObjectException.class,
                () -> roomTypeService.save(new RoomTypeRequest("lux")));
    }

    @Test
    @DisplayName("Find all room types")
    void findAllTest() {
        assertEquals(4, roomTypeService.findAll().size());
    }

    @Test
    @DisplayName("Successful find room type by type")
    void findByTypeTest() {
        Long id = testDBUtils.getRoomTypeIdByType("lux");
        assertEquals(id, roomTypeService.findByType("lux").getId());
    }

    @Test
    @DisplayName("Fail find room type by type")
    void findByTypeThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomTypeService.findByType("new lux"));
    }

    @Test
    @DisplayName("Successful find room type by id")
    void findByIdTest() {
        Long id = testDBUtils.getRoomTypeIdByType("lux");
        assertEquals("lux", roomTypeService.findById(id).getType());
    }

    @Test
    @DisplayName("Fail find room type by id")
    void findByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomTypeService.findById(100L));
    }

    @Test
    @DisplayName("Successful update room type")
    void updateTest() {
        Long id = testDBUtils.getRoomTypeIdByType("lux");
        roomTypeService.update(id, new RoomTypeRequest("new LUx"));
        assertAll(
                () -> assertNotNull(roomTypeService.findByType("new lux")),
                () -> assertEquals(4, roomTypeService.findAll().size())
        );
    }

    @Test
    @DisplayName("Fail update room type (throw NoSuchElementException)")
    void updateThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomTypeService.update(100L, new RoomTypeRequest("lux")));
    }

    @Test
    @DisplayName("Fail update room type (throw DuplicateObjectException)")
    void updateThrowDuplicateObjectExceptionTest() {
        Long id = testDBUtils.getRoomTypeIdByType("standart single");
        assertThrows(DuplicateObjectException.class,
                () -> roomTypeService.update(id, new RoomTypeRequest("lux")));
    }

    @Test
    @DisplayName("Successful delete room type")
    void deleteByIdTest() {
        Long id = testDBUtils.getRoomTypeIdByType("king");
        roomTypeService.deleteById(id);
        assertEquals(3, roomTypeService.findAll().size());
    }

    @Test
    @DisplayName("Fail delete room type (throw NoSuchElementException)")
    void deleteByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> roomTypeService.deleteById(100L));
    }

    @Test
    @DisplayName("Fail delete room type (throw NotEmptyObjectException)")
    void deleteByIdThrowNotEmptyObjectExceptionTest() {
        Long id = testDBUtils.getRoomTypeIdByType("standart single");
        assertThrows(NotEmptyObjectException.class,
                () -> roomTypeService.deleteById(id));
    }
}