package com.robot.hotel.guest;

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
class GuestServiceTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    GuestService guestService;

    @Autowired
    private DBInitializer dbInitializer;

    @Autowired
    private TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all guests")
    void findAllTest() {
        assertEquals(5, guestService.findAll().size());
    }

    @Test
    @DisplayName("Successful create new guest")
    void saveTest() {
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "");
        assertAll(
                () -> assertNotNull(guestService.save(guestRequest).getId()),
                () -> assertEquals(6, guestService.findAll().size())
        );
    }

    @Test
    @DisplayName("Fail create new guest (throw DuplicateObjectException - wrong email)")
    void saveThrowDuplicateObjectExceptionWrongEmailTest() {
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "(095)345-34-34", "sidor@gmail.com", "");
        assertThrows(DuplicateObjectException.class,
                () -> guestService.save(guestRequest));
    }

    @Test
    @DisplayName("Fail create new guest (throw DuplicateObjectException - wrong tel. number)")
    void saveThrowDuplicateObjectExceptionWrongTelTest() {
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "096-546-78-34", "semenov@gmail.com", "");
        assertThrows(DuplicateObjectException.class,
                () -> guestService.save(guestRequest));
    }

    @Test
    @DisplayName("Fail create new guest (throw DuplicateObjectException - wrong passport)")
    void saveThrowDuplicateObjectExceptionWrongPassportTest() {
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "va123456");
        assertThrows(DuplicateObjectException.class,
                () -> guestService.save(guestRequest));
    }

    @Test
    @DisplayName("Successful find guest by id")
    void findByIdTest() {
        Long id = testDBUtils.getGuestIdByEmail("sidor@gmail.com");
        assertEquals("0965467834", guestService.findById(id).getTelNumber());
    }

    @Test
    @DisplayName("Fail find guest by id")
    void findByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> guestService.findById(200L));
    }

    @Test
    @DisplayName("Successful find guest by email")
    void findByEmailTest() {
        assertEquals("0965467834", guestService.findByEmail("sidor@gmail.com").getTelNumber());
    }

    @Test
    @DisplayName("Fail find guest by email")
    void findByEmailThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> guestService.findByEmail("sidor1234@gmail.com"));
    }

    @Test
    @DisplayName("Successful find guest by tel.number")
    void findByTelNumberTest() {
        assertEquals("sidor@gmail.com", guestService.findByTelNumber("(096)546-78-34").getEmail());
    }

    @Test
    @DisplayName("Fail find guest by tel.number")
    void findByTelNumberThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> guestService.findByTelNumber("0937564235"));
    }

    @Test
    @DisplayName("Successful find guest by passport serial number")
    void findByPassportSerialNumberTest() {
        assertEquals("sidor_andr@gmail.com", guestService.findByPassportSerialNumber("bb345678").getEmail());
    }

    @Test
    @DisplayName("Fail find guest by passport serial number")
    void findByPassportSerialNumberThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> guestService.findByPassportSerialNumber("df345123"));
    }

    @Test
    @DisplayName("Find guest by lastname")
    void findByLastNameTest() {
        assertEquals(2, guestService.findByLastName("sidorov").size());
    }

    @Test
    @DisplayName("Find guest by reservation")
    void findGuestByReservationTest() {
        Long id = testDBUtils.getReservationIdByRoom("101");
        assertEquals(3, guestService.findGuestByReservation(id).size());
    }

    @Test
    @DisplayName("Successful update guest")
    void updateTest() {
        Long id = testDBUtils.getGuestIdByEmail("sidor@gmail.com");
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "");

        guestService.update(id, guestRequest);
        assertAll(
                () -> assertEquals("dmitro", guestService.findById(id).getFirstName()),
                () -> assertEquals(5, guestService.findAll().size())
        );
    }

    @Test
    @DisplayName("Fail update guest (throw NoSuchElementException)")
    void updateThrowNoSuchElementExceptionTest() {
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "");

        assertThrows(NoSuchElementException.class,
                () -> guestService.update(100L, guestRequest));
    }

    @Test
    @DisplayName("Fail update guest (throw DuplicateObjectException - wrong email)")
    void updateThrowDuplicateObjectExceptionWrongEmailTest() {
        Long id = testDBUtils.getGuestIdByEmail("sidor@gmail.com");
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "(095)345-34-34", "kozlov@gmail.com", "");

        assertThrows(DuplicateObjectException.class,
                () -> guestService.update(id, guestRequest));
    }

    @Test
    @DisplayName("Fail update guest (throw DuplicateObjectException - wrong tel. number)")
    void updateThrowDuplicateObjectExceptionWrongTelTest() {
        Long id = testDBUtils.getGuestIdByEmail("sidor@gmail.com");
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "0964569034", "semenov@gmail.com", "");

        assertThrows(DuplicateObjectException.class,
                () -> guestService.update(id, guestRequest));
    }

    @Test
    @DisplayName("Fail update guest (throw DuplicateObjectException - wrong passport)")
    void updateThrowDuplicateObjectExceptionWrongPassportTest() {
        Long id = testDBUtils.getGuestIdByEmail("sidor@gmail.com");
        GuestRequest guestRequest = new GuestRequest("dmitro", "semenov",
                "(095)345-34-34", "semenov@gmail.com", "ba345863");

        assertThrows(DuplicateObjectException.class,
                () -> guestService.update(id, guestRequest));
    }

    @Test
    @DisplayName("Successful delete guest")
    void deleteByIdTest() {
        Long id = testDBUtils.getGuestIdByEmail("dmitr@gmail.com");
        guestService.deleteById(id);
        assertEquals(4, guestService.findAll().size());
    }

    @Test
    @DisplayName("Fail delete guest (throw NoSuchElementException)")
    void deleteByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> guestService.deleteById(100L));
    }

    @Test
    @DisplayName("Fail delete guest (throw NotEmptyObjectException)")
    void deleteByIdThrowNotEmptyObjectExceptionTest() {
        Long id = testDBUtils.getGuestIdByEmail("sidor@gmail.com");

        assertThrows(NotEmptyObjectException.class,
                () -> guestService.deleteById(id));
    }
}