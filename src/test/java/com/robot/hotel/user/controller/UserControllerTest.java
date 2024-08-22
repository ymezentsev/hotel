package com.robot.hotel.user.controller;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBAuthentication;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.dto.password.ForgotPasswordRequestDto;
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
class UserControllerTest {
    @LocalServerPort
    Integer port;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @Autowired
    DBAuthentication DBAuthentication;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        DBAuthentication.loginUser();
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/users";
    }

    @Test
    @DisplayName("Find all users")
    void findAllTest() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(6));
    }

    @Test
    @DisplayName("Find user by id")
    void findByIdTest() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .pathParam("id", testDBUtils.getUserIdByEmail("sidor@gmail.com"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("lastName", equalTo("sidorov"));
    }

    @Test
    @DisplayName("Successful search users")
    void searchTest() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .params("lastnames", "sidor")
                .when().get("/search")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(2));
    }

    @Test
    @DisplayName("Successful update user")
    void updateTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(registrationRequestDto)
                .pathParam("id", testDBUtils.getUserIdByEmail("kozlov@gmail.com"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update user (incorrect user input)")
    void updateWithIncorrectDataTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenovgmail.com", "Password1", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(registrationRequestDto)
                .pathParam("id", testDBUtils.getUserIdByEmail("sidor@gmail.com"))
                .when().put("/{id}")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid email"));
    }

    @Test
    @DisplayName("Delete user")
    void deleteByIdTest() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .pathParam("id", testDBUtils.getUserIdByEmail("dmitr@gmail.com"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Successful send forgot password email")
    void forgotPasswordTest() {
        given().contentType(ContentType.JSON)
                .body(new EmailRequestDto("sidor@gmail.com"))
                .when().post("/forgot-password")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail send forgot password email (incorrect user input)")
    void forgotPasswordWithIncorrectDataTest() {
        given().contentType(ContentType.JSON)
                .body(new EmailRequestDto("sidor@gmailcom"))
                .when().post("/forgot-password")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid email"));
    }

    @Test
    @DisplayName("Successful reset password and set new one")
    void resetPasswordTest() {
        given().contentType(ContentType.JSON)
                .body(new ForgotPasswordRequestDto("newPassword1", "newPassword1"))
                .param("token", "8ac319b4-990f-466f-8a5a-7c2a028b430c")
                .when().put("/reset-password")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail reset password and set new one (incorrect user input)")
    void resetPasswordWithIncorrectDataTest() {
        given().contentType(ContentType.JSON)
                .body(new ForgotPasswordRequestDto("password", "newPassword1"))
                .param("token", "8ac319b4-990f-466f-8a5a-7c2a028b430c")
                .when().put("/reset-password")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Passwords do not match"));
    }

    @Test
    @DisplayName("Successful change password")
    void changePasswordTest() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(new ChangePasswordRequestDto("Qwerty123456",
                        "newPassword1",
                        "newPassword1"))
                .when().put("/change-password")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail change password (incorrect user input)")
    void changePasswordWithIncorrectDataTest() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(new ChangePasswordRequestDto("Qwerty123456",
                        "newPassword",
                        "Password"))
                .when().put("/change-password")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Passwords do not match"));
    }
}