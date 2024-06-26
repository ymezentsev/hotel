package com.robot.hotel.user;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.user.dto.UserRequest;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,  classes = ContainerConfiguration.class)
class UserControllerTest {
    @LocalServerPort
    Integer port;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        RestAssured.baseURI = "http://localhost:" + port + "/users";
    }

    @Test
    @DisplayName("Find all users")
    void findAllTest() {
        given().contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(6));
    }

    @Test
    @DisplayName("Successful create new user")
    void saveTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .body(userRequest)
                .when().post()
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Fail create new user (incorrect user input)")
    void saveWithIncorrectDataTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenovgmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .body(userRequest)
                .when().post()
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid email"));
    }

    @Test
    @DisplayName("Find user by id")
    void findByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getUserIdByEmail("sidor@gmail.com"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("lastName", equalTo("sidorov"));
    }

    @Test
    @DisplayName("Find user by email")
    void findByEmailTest() {
        given().contentType(ContentType.JSON)
                .pathParam("email", "sidor@gmail.com")
                .when().get("/email/{email}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("lastName", equalTo("sidorov"));
    }

    @Test
    @DisplayName("Find user by phone number")
    void findByPhoneNumberTest() {
        given().contentType(ContentType.JSON)
                .pathParam("phoneNumber", "+380965467834")
                .when().get("/phoneNumber/{phoneNumber}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("lastName", equalTo("sidorov"));
    }

    @Test
    @DisplayName("Find user by passport serial number")
    void findByPassportSerialNumberTest() {
        given().contentType(ContentType.JSON)
                .pathParam("passportSerialNumber", "va123456")
                .when().get("/passport/{passportSerialNumber}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("lastName", equalTo("dmitrenko"));
    }

    @Test
    @DisplayName("Find user by lastname")
    void findByLastNameTest() {
        given().contentType(ContentType.JSON)
                .pathParam("lastName", "sidorov")
                .when().get("/lastName/{lastName}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(2));
    }

    @Test
    @DisplayName("Find users by reservation")
    void findUsersByReservationTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getReservationIdByRoom("204"))
                .when().get("/reservations/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(2));
    }

    @Test
    @DisplayName("Find users by role")
    void findUsersByRoleTest() {
        given().contentType(ContentType.JSON)
                .pathParam("role", "USER")
                .when().get("/role/{role}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(4));
    }

    @Test
    @DisplayName("Successful update user")
    void updateTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .body(userRequest)
                .pathParam("id", testDBUtils.getUserIdByEmail("kozlov@gmail.com"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update user (incorrect user input)")
    void updateWithIncorrectDataTest() {
        UserRequest userRequest = new UserRequest("dmitro", "semenov", "+1",
                "0953453434", "semenovgmail.com", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        given().contentType(ContentType.JSON)
                .body(userRequest)
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
                .pathParam("id", testDBUtils.getUserIdByEmail("dmitr@gmail.com"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}