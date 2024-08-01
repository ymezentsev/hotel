package com.robot.hotel.user.controller;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class AuthenticationControllerTest {
    @LocalServerPort
    Integer port;

    @Autowired
    DBInitializer dbInitializer;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/auth";
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Successful register new user")
    void registerTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov12345@gmail.com", "Password1", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .body(registrationRequestDto)
                .when().post("/register")
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Fail register new user (incorrect user input)")
    void registerWithIncorrectDataTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenovgmail.com", "Password1", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .body(registrationRequestDto)
                .when().post("/register")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid email"));
    }

    @Test
    @DisplayName("Successful confirm new user's email")
    void confirmTest() {
        given().contentType(ContentType.JSON)
                .param("token","6453fbfb-8ff9-4dea-b8c9-notConfirmed")
                .when().get("/confirm")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Successful resend new confirmation email")
    void resendConfirmationEmailTest() {
        given().contentType(ContentType.JSON)
                .body(new EmailRequestDto("sidor@gmail.com"))
                .when().post("/resend-email-confirmation")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail resend new confirmation email (incorrect email)")
    void resendConfirmationEmailWithIncorrectDataTest() {
        given().contentType(ContentType.JSON)
                .body(new EmailRequestDto("nikolagmail.com"))
                .when().post("/resend-email-confirmation")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid email"));
    }
}