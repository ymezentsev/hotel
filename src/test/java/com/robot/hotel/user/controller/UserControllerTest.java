package com.robot.hotel.user.controller;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBAuthentication;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBUtils;
import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.UpdateUserRequestDto;
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
    DBUtils dbUtils;

    @Autowired
    DBAuthentication dbAuthentication;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/users";
    }

    @Test
    @DisplayName("Find all users")
    void findAllTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(6));
    }

    @Test
    @DisplayName("Find user by id")
    void findByIdTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", dbUtils.getUserIdByEmail("sidor@gmail.com"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("lastName", equalTo("sidorov"));
    }

    @Test
    @DisplayName("Successful search users")
    void searchTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginAdmin();
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto("dmitro", "semenov",
                "+1", "0953453434",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .body(updateUserRequestDto)
                .pathParam("id", dbUtils.getUserIdByEmail("kozlov@gmail.com"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update user (incorrect user input)")
    void updateWithIncorrectDataTest() {
        dbAuthentication.loginAdmin();
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto("dmitro", "semenov",
                "+1", "0953453434t",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .body(updateUserRequestDto)
                .pathParam("id", dbUtils.getUserIdByEmail("sidor@gmail.com"))
                .when().put("/{id}")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid phone number"));
    }

    @Test
    @DisplayName("Delete user")
    void deleteByIdTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", dbUtils.getUserIdByEmail("dmitr@gmail.com"))
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
        dbAuthentication.loginUser();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginUser();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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