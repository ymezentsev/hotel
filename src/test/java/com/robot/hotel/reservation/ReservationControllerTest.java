package com.robot.hotel.reservation;

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
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,  classes = ContainerConfiguration.class)
class ReservationControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        RestAssured.baseURI = "http://localhost:" + port + "/reservations";
    }

    @Test
    @DisplayName("Find all reservations")
    void findAllTest() {
        given().contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(4));
    }

    @Test
    @DisplayName("Find reservation by id")
    void findByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getReservationIdByRoom("203"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("checkInDate", equalTo(LocalDate.now().plusDays(4).toString()));
    }

    @Test
    @DisplayName("Successful create new reservation")
    void saveTest() {
        Long user1Id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        Long user2Id = testDBUtils.getUserIdByEmail("sidor_andr@gmail.com");

        ReservationRequest reservationRequest = new ReservationRequest("201", LocalDate.now(),
                LocalDate.now().plusDays(1), Set.of(user1Id.toString(), user2Id.toString()));

        given().contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post()
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Fail create new reservation (incorrect user input)")
    void saveWithIncorrectDataTest() {
        ReservationRequest reservationRequest = new ReservationRequest("", LocalDate.now(),
                LocalDate.now().plusDays(1), Set.of());

        given().contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post()
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Room number is required"));
    }

    @Test
    @DisplayName("Find reservations by user id")
    void findReservationsByUserIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getUserIdByEmail("sidor_andr@gmail.com"))
                .when().get("/user/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(3));
    }

    @Test
    @DisplayName("Find reservations by room")
    void findReservationsByRoomTest() {
        given().contentType(ContentType.JSON)
                .pathParam("roomNumber", "204")
                .when().get("/room/{roomNumber}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(2));
    }

    @Test
    @DisplayName("Find all current reservations")
    void findCurrentReservationsTest() {
        given().contentType(ContentType.JSON)
                .when().get("/current")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(3));
    }

    @Test
    @DisplayName("Find all current reservations for a specific room")
    void findCurrentReservationsForSpecificRoomTest() {
        given().contentType(ContentType.JSON)
                .pathParam("roomNumber", "204")
                .when().get("/current/room/{roomNumber}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(1));
    }

    @Test
    @DisplayName("Delete reservation")
    void deleteByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getReservationIdByRoom("101"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}