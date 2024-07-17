package com.robot.hotel.user;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RegistrationServiceImplTest {
    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserService userService;

    @Autowired
    DBInitializer dbInitializer;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Successful create new user with passport")
    void saveWithPassportTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "usa", LocalDate.of(2018, 3, 8));
        assertAll(
                () -> assertNotNull(registrationService.register(registrationRequestDto).id()),
                () -> assertEquals(7, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Successful create new user without passport")
    void saveWithoutPassportTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                null, null, null);
        assertAll(
                () -> assertNotNull(registrationService.register(registrationRequestDto).id()),
                () -> assertEquals(7, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong email)")
    void saveThrowDuplicateObjectExceptionWrongEmailTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "sidor@gmail.com", "Password1", "Password1",
                "df123456", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> registrationService.register(registrationRequestDto));
    }

    @Test
    @DisplayName("Fail create new user (throw DuplicateObjectException - wrong phone number)")
    void saveThrowDuplicateObjectExceptionWrongPhoneTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "965467834", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> registrationService.register(registrationRequestDto));
    }
}