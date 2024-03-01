package com.robot.hotel.guest;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class GuestControllerTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");

    @LocalServerPort
    private Integer port;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        RestAssured.baseURI = "http://localhost:" + port + "/guests";
    }

    @Test
    @DisplayName("Find all guests")
    void findAllTest() {
        given().contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(5));
    }

    @Test
    @DisplayName("Successful create new guest")
    void saveTest() {
        GuestRequest guestRequest = new GuestRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andr@gmail.com", "");

        given().contentType(ContentType.JSON)
                .body(guestRequest)
                .when().post()
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Fail create new guest (incorrect user input)")
    void saveWithIncorrectDataTest() {
        GuestRequest guestRequest = new GuestRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andrgmail.com", "");

        given().contentType(ContentType.JSON)
                .body(guestRequest)
                .when().post()
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid email"));
    }

    @Test
    @DisplayName("Find guest by id")
    void findByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getGuestIdByEmail("sidor@gmail.com"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("lastName", equalTo("sidorov"));
    }

    @Test
    @DisplayName("Find guest by email")
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
    @DisplayName("Find guest by tel.number")
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
    @DisplayName("Find guest by passport serial number")
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
    @DisplayName("Find guest by lastname")
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
    @DisplayName("Find guest by reservation")
    void findGuestByReservationTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getReservationIdByRoom("204"))
                .when().get("/reservations/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(2));
    }

    @Test
    @DisplayName("Successful update guest")
    void updateTest() {
        GuestRequest guestRequest = new GuestRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andr@gmail.com", "");

        given().contentType(ContentType.JSON)
                .body(guestRequest)
                .pathParam("id", testDBUtils.getGuestIdByEmail("kozlov@gmail.com"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update guest (incorrect user input)")
    void updateWithIncorrectDataTest() {
        GuestRequest guestRequest = new GuestRequest("Dmitro", "Andriev",
                "(096)456-32-74", "Andrgmail.com", "");

        given().contentType(ContentType.JSON)
                .body(guestRequest)
                .pathParam("id", testDBUtils.getGuestIdByEmail("sidor@gmail.com"))
                .when().put("/{id}")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Not valid email"));
    }

    @Test
    @DisplayName("Delete guest")
    void deleteByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getGuestIdByEmail("dmitr@gmail.com"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}