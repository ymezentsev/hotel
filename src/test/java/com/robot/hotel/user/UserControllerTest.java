package com.robot.hotel.user;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,  classes = ContainerConfiguration.class)
class UserControllerTest {
    @LocalServerPort
    private Integer port;

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
                .body("size()", is(5));
    }

    @Test
    @DisplayName("Successful create new user")
    void saveTest() {
        UserRequest userRequest = new UserRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andr@gmail.com", "");

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
        UserRequest userRequest = new UserRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andrgmail.com", "");

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
    @DisplayName("Find user by tel.number")
    void findByTelNumberTest() {
        given().contentType(ContentType.JSON)
                .pathParam("telNumber", "0965467834")
                .when().get("/telNumber/{telNumber}")
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
                .body("size()", is(2));
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
                .body("size()", is(2));
    }

    @Test
    @DisplayName("Successful update user")
    void updateTest() {
        UserRequest userRequest = new UserRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andr@gmail.com", "");

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
        UserRequest userRequest = new UserRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andrgmail.com", "");

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