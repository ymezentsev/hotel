package com.robot.hotel.user.service.impl;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBUtils;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NoSuchElementException;
import com.robot.hotel.exception.NotEnoughInformationException;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.service.PassportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class PassportServiceImplTest {
    @Autowired
    PassportService passportService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    DBUtils dbUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Successful create new passport")
    void getPassportFromUserRequestTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "usa", LocalDate.of(2018, 3, 8));

        assertNotNull(passportService.getPassportFromUserRequest(registrationRequestDto.getPassportSerialNumber(),
                registrationRequestDto.getCountryCode(),
                registrationRequestDto.getIssueDate(),
                null));
    }

    @Test
    @DisplayName("Get null if RegistrationRequestDto doesn't consist passport info")
    void getNullPassportFromUserRequestTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                null, null, null);
        assertNull(passportService.getPassportFromUserRequest(registrationRequestDto.getPassportSerialNumber(),
                registrationRequestDto.getCountryCode(),
                registrationRequestDto.getIssueDate(),
                null));
    }

    @Test
    @DisplayName("Fail get new passport (throw NotEnoughInformationException - not all information for creating passport)")
    void getPassportFromUserRequestThrowNotEnoughInformationExceptionTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "df123456", null, LocalDate.of(2018, 3, 8));
        assertThrows(NotEnoughInformationException.class,
                () -> passportService.getPassportFromUserRequest(registrationRequestDto.getPassportSerialNumber(),
                        registrationRequestDto.getCountryCode(),
                        registrationRequestDto.getIssueDate(),
                        null));
    }

    @Test
    @DisplayName("Fail create new passport (throw DuplicateObjectException for new user)")
    void getPassportFromUserRequestThrowDuplicateObjectExceptionUserIdIsNullTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "bb345678", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> passportService.getPassportFromUserRequest(registrationRequestDto.getPassportSerialNumber(),
                        registrationRequestDto.getCountryCode(),
                        registrationRequestDto.getIssueDate(),
                        null));
    }

    @Test
    @DisplayName("Fail create new passport (throw DuplicateObjectException for existing user)")
    void getPassportFromUserRequestThrowDuplicateObjectExceptionUserIdIsNotNullTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "bb345678", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> passportService.getPassportFromUserRequest(registrationRequestDto.getPassportSerialNumber(),
                        registrationRequestDto.getCountryCode(),
                        registrationRequestDto.getIssueDate(),
                        dbUtils.getUserIdByEmail("dmitr@gmail.com")));
    }

    @Test
    @DisplayName("Fail create new user (throw NoSuchElementException - wrong country code for passport)")
    void saveThrowNoSuchElementExceptionWrongCountryCodeTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "UKK", LocalDate.of(2018, 3, 8));
        assertThrows(NoSuchElementException.class,
                () -> passportService.getPassportFromUserRequest(registrationRequestDto.getPassportSerialNumber(),
                        registrationRequestDto.getCountryCode(),
                        registrationRequestDto.getIssueDate(),
                        null));
    }
}