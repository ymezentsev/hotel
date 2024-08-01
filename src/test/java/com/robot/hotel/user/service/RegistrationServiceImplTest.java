package com.robot.hotel.user.service;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.FailedToSendEmailException;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.repository.ConfirmationTokenRepository;
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
class RegistrationServiceImplTest {
    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserService userService;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Successful register new user with passport")
    void registerWithPassportTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov12345@gmail.com", "Password1", "Password1",
                "df123456", "usa", LocalDate.of(2018, 3, 8));
        assertAll(
                () -> assertNotNull(registrationService.register(registrationRequestDto).id()),
                () -> assertEquals(7, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Successful register new user without passport")
    void registerWithoutPassportTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov12345@gmail.com", "Password1", "Password1",
                null, null, null);
        assertAll(
                () -> assertNotNull(registrationService.register(registrationRequestDto).id()),
                () -> assertEquals(7, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Fail register new user (throw DuplicateObjectException - wrong email)")
    void registerThrowDuplicateObjectExceptionWrongEmailTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "sidor@gmail.com", "Password1", "Password1",
                "df123456", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> registrationService.register(registrationRequestDto));
    }

    @Test
    @DisplayName("Fail register new user (throw DuplicateObjectException - wrong phone number)")
    void registerThrowDuplicateObjectExceptionWrongPhoneTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "965467834", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "USA", LocalDate.of(2018, 3, 8));
        assertThrows(DuplicateObjectException.class,
                () -> registrationService.register(registrationRequestDto));
    }

    @Test
    @DisplayName("Successful confirm token")
    void confirmTokenTest() {
        assertAll(
                () -> assertFalse(testUtils.getUserByEmail("nikola@gmail.com").isEnabled()),
                () -> assertNull(confirmationTokenService
                        .getConfirmationToken("6453fbfb-8ff9-4dea-b8c9-notConfirmed").getConfirmedAt())
        );

        registrationService.confirmToken("6453fbfb-8ff9-4dea-b8c9-notConfirmed");
        assertAll(
                () -> assertTrue(testUtils.getUserByEmail("nikola@gmail.com").isEnabled()),
                () -> assertNotNull(confirmationTokenService
                        .getConfirmationToken("6453fbfb-8ff9-4dea-b8c9-notConfirmed").getConfirmedAt())
        );
    }

    @Test
    @DisplayName("Successful send confirmation email")
    void sendConfirmationEmailTest() {
        registrationService.sendConfirmationEmail("sidor@gmail.com");
        assertEquals(5, confirmationTokenRepository.findAll().size());
    }

    @Test
    @DisplayName("Failed send confirmation email (throws NoSuchElementException)")
    void sendConfirmationEmailThrowsNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> registrationService.sendConfirmationEmail("newTest@mail"));
    }

    @Test
    @DisplayName("Failed send confirmation email (throws FailedToSendEmailException)")
    void sendConfirmationEmailThrowsFailedToSendEmailExceptionTest() {
        assertThrows(FailedToSendEmailException.class,
                () -> registrationService.sendConfirmationEmail("admin@gmail.com"));
    }
}