package com.robot.hotel.user;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.NotEnoughInformationException;
import com.robot.hotel.user.dto.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class UserServiceImplTest {
    @Autowired
    UserService userService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
       dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all users")
    void findAllTest() {
        assertEquals(6, userService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Successful create new user with passport")
    void saveWithPassportTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "usa", LocalDate.of(2018, 3, 8));
        assertAll(
                () -> assertNotNull(userService.save(userRequest).id()),
                () -> assertEquals(7, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Successful create new user without passport")
    void saveWithoutPassportTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                null, null, null);
        assertAll(
                () -> assertNotNull(userService.save(userRequest).id()),
                () -> assertEquals(7, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Fail create new user (throw NoSuchElementException - wrong phone code)")
    void saveThrowNoSuchElementExceptionWrongPhoneCodeTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+999",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));
        assertThrows(NoSuchElementException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Fail create new user (throw NoSuchElementException - wrong country code for passport)")
    void saveThrowNoSuchElementExceptionWrongCountryCodeTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "UKK", LocalDate.of(2018, 3, 8));
        assertThrows(NoSuchElementException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong email)")
    void saveThrowDuplicateObjectExceptionWrongEmailTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "sidor@gmail.com", "Password1",
                "df123456", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong phone number)")
    void saveThrowDuplicateObjectExceptionWrongPhoneTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "965467834", "semenov@gmail.com", "Password1",
                "df123456", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong passport)")
    void saveThrowDuplicateObjectExceptionWrongPassportTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "bb345678", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Fail create new user (throw NotEnoughInformationException - not all information for creating passport)")
    void saveThrowNotEnoughInformationExceptionTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", null, LocalDate.of(2018, 3, 8));
        assertThrows(NotEnoughInformationException.class,
                () -> userService.save(userRequest));
    }

    @Test
    @DisplayName("Successful find user by id")
    void findByIdTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        assertEquals("+380965467834", userService.findById(id).phoneNumber());
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
        assertEquals("+380965467834", userService.findByEmail("sidor@gmail.com").phoneNumber());
    }

    @Test
    @DisplayName("Fail find user by email")
    void findByEmailThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail("sidor1234@gmail.com"));
    }

    @Test
    @DisplayName("Successful find user by phone number")
    void findByPhoneNumberTest() {
        assertEquals("sidor@gmail.com", userService.findByPhoneNumber("965467834").email());
    }

    @Test
    @DisplayName("Fail find user by phone number")
    void findByPhoneNumberThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByPhoneNumber("0937564235"));
    }

    @Test
    @DisplayName("Successful find user by full phone number")
    void findByFullPhoneNumberTest() {
        assertEquals("sidor@gmail.com", userService.findByPhoneNumber("+380965467834").email());
    }

    @Test
    @DisplayName("Fail find user by full phone number")
    void findByFullPhoneNumberThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByPhoneNumber("+3800937564235"));
    }

    @Test
    @DisplayName("Successful find user by passport serial number")
    void findByPassportSerialNumberTest() {
        assertEquals("sidor_andr@gmail.com", userService.findByPassportSerialNumber("bb345678").email());
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
        assertEquals(2, userService
                .findByLastName("sidorov", Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Find user by reservation")
    void findUsersByReservationTest() {
        Long id = testDBUtils.getReservationIdByRoom("101");
        assertEquals(3, userService.findUsersByReservation(id, Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Successful find users by role")
    void findUsersByRoleTest() {
        assertEquals(4, userService
                .findUsersByRole("user", Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Fail find users by role")
    void findUsersByRoleThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findUsersByRole("NEW_USER", Pageable.unpaged()));
    }

    @Test
    @DisplayName("Successful update user")
    void updateTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        userService.update(id, userRequest);
        assertAll(
                () -> assertEquals("dmitro", userService.findById(id).firstName()),
                () -> assertEquals(6, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Fail update user (throw NoSuchElementException - wrong user id)")
    void updateThrowNoSuchElementExceptionWrongUserIdTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "usa", LocalDate.of(2018, 3, 8));

        assertThrows(NoSuchElementException.class,
                () -> userService.update(100L, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw NoSuchElementException - wrong phone code)")
    void updateThrowNoSuchElementExceptionWrongPhoneCodeTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+999",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));
        assertThrows(NoSuchElementException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw NoSuchElementException - wrong country code for passport)")
    void updateThrowNoSuchElementExceptionWrongCountryCodeTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "UKK", LocalDate.of(2018, 3, 8));
        assertThrows(NoSuchElementException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong email)")
    void updateThrowDuplicateObjectExceptionWrongEmailTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "kozlov@gmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong phone number)")
    void updateThrowDuplicateObjectExceptionWrongPhoneTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "505463213", "semenov@gmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong passport)")
    void updateThrowDuplicateObjectExceptionWrongPassportTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "bb345678", "UKR", LocalDate.of(2018, 3, 8));

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, userRequest));
    }

    @Test
    @DisplayName("Successful delete user")
    void deleteByIdTest() {
        Long id = testDBUtils.getUserIdByEmail("dmitr@gmail.com");
        userService.deleteById(id);
        assertEquals(5, userService.findAll(Pageable.unpaged()).getTotalElements());
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