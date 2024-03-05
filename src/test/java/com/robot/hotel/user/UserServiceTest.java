package com.robot.hotel.user;

import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserServiceTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    UserService userService;

    @Autowired
    private DBInitializer dbInitializer;

    @Autowired
    private TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all users")
    void findAllTest() {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    @DisplayName("Successful create new user")
    void saveTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "");
        assertAll(
                () -> assertNotNull(userService.save(userRequest).getId()),
                () -> assertEquals(6, userService.findAll().size())
        );
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong email)")
    void saveThrowDuplicateObjectExceptionWrongEmailTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "(095)345-34-34", "sidor@gmail.com", "");
        assertThrows(DuplicateObjectException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong tel. number)")
    void saveThrowDuplicateObjectExceptionWrongTelTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "096-546-78-34", "semenov@gmail.com", "");
        assertThrows(DuplicateObjectException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong passport)")
    void saveThrowDuplicateObjectExceptionWrongPassportTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "va123456");
        assertThrows(DuplicateObjectException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Successful find user by id")
    void findByIdTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        assertEquals("0965467834", userService.findById(id).getTelNumber());
    }

    @Test
    @DisplayName("Fail find user by id")
    void findByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findById(200L));
    }

    @Test
    @DisplayName("Successful find user by email")
    void findByEmailTest() {
        assertEquals("0965467834", userService.findByEmail("sidor@gmail.com").getTelNumber());
    }

    @Test
    @DisplayName("Fail find user by email")
    void findByEmailThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail("sidor1234@gmail.com"));
    }

    @Test
    @DisplayName("Successful find user by tel.number")
    void findByTelNumberTest() {
        assertEquals("sidor@gmail.com", userService.findByTelNumber("(096)546-78-34").getEmail());
    }

    @Test
    @DisplayName("Fail find user by tel.number")
    void findByTelNumberThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByTelNumber("0937564235"));
    }

    @Test
    @DisplayName("Successful find user by passport serial number")
    void findByPassportSerialNumberTest() {
        assertEquals("sidor_andr@gmail.com", userService.findByPassportSerialNumber("bb345678").getEmail());
    }

    @Test
    @DisplayName("Fail find user by passport serial number")
    void findByPassportSerialNumberThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByPassportSerialNumber("df345123"));
    }

    @Test
    @DisplayName("Find user by lastname")
    void findByLastNameTest() {
        assertEquals(2, userService.findByLastName("sidorov").size());
    }

    @Test
    @DisplayName("Find user by reservation")
    void findGuestByReservationTest() {
        Long id = testDBUtils.getReservationIdByRoom("101");
        assertEquals(3, userService.findUsersByReservation(id).size());
    }

    @Test
    @DisplayName("Successful update user")
    void updateTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "");

        userService.update(id, userRequest);
        assertAll(
                () -> assertEquals("dmitro", userService.findById(id).getFirstName()),
                () -> assertEquals(5, userService.findAll().size())
        );
    }

    @Test
    @DisplayName("Fail update user (throw NoSuchElementException)")
    void updateThrowNoSuchElementExceptionTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "");

        assertThrows(NoSuchElementException.class,
                () -> userService.update(100L, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong email)")
    void updateThrowDuplicateObjectExceptionWrongEmailTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "(095)345-34-34", "kozlov@gmail.com", "");

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong tel. number)")
    void updateThrowDuplicateObjectExceptionWrongTelTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "0964569034", "semenov@gmail.com", "");

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong passport)")
    void updateThrowDuplicateObjectExceptionWrongPassportTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "ba345863");

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Successful delete user")
    void deleteByIdTest() {
        Long id = testDBUtils.getUserIdByEmail("dmitr@gmail.com");
        userService.deleteById(id);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    @DisplayName("Fail delete user (throw NoSuchElementException)")
    void deleteByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.deleteById(100L));
    }

    @Test
    @DisplayName("Fail delete user (throw NotEmptyObjectException)")
    void deleteByIdThrowNotEmptyObjectExceptionTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");

        assertThrows(NotEmptyObjectException.class,
                () -> userService.deleteById(id));
    }
}