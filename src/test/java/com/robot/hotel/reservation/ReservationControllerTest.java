package com.robot.hotel.reservation;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBAuthentication;
import com.robot.hotel.DBUtils;
import com.robot.hotel.reservation.dto.ReservationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class ReservationControllerTest {
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
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/reservations";
    }

    @Test
    @DisplayName("Find all reservations")
    void findAllTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .log().body()
                .body("content.size()", is(4));
    }

    @Test
    @DisplayName("Find reservation by id")
    void findByIdTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", dbUtils.getReservationIdByRoom("203"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("checkInDate", equalTo(LocalDate.now().plusDays(4).toString()));
    }

    @Test
    @DisplayName("Successful create new reservation")
    void saveTest() {
        dbAuthentication.loginUser();
        ReservationRequest reservationRequest = new ReservationRequest("201", LocalDate.now(),
                LocalDate.now().plusDays(1), Set.of("sidor@gmail.com", "sidor_andr@gmail.com"));

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginUser();
        ReservationRequest reservationRequest = new ReservationRequest("", LocalDate.now(),
                LocalDate.now().plusDays(1), Set.of());

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", dbUtils.getUserIdByEmail("sidor_andr@gmail.com"))
                .when().get("/user/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(3));
    }

    @Test
    @DisplayName("Find reservations by room")
    void findReservationsByRoomTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("roomNumber", "204")
                .when().get("/room/{roomNumber}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(2));
    }

    @Test
    @DisplayName("Find all current reservations")
    void findCurrentReservationsTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .when().get("/current")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(3));
    }

    @Test
    @DisplayName("Find all current reservations for a specific room")
    void findCurrentReservationsForSpecificRoomTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("roomNumber", "204")
                .when().get("/current/room/{roomNumber}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(1));
    }

    @Test
    @DisplayName("Delete reservation")
    void deleteByIdTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", dbUtils.getReservationIdByRoom("101"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}