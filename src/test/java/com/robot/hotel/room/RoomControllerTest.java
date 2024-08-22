package com.robot.hotel.room;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBAuthentication;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class RoomControllerTest {
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
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/rooms";
    }

    @Test
    @DisplayName("Find all rooms")
    void findAllTest() {
        given().contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(5));
    }

    @Test
    @DisplayName("Successful create new room")
    void saveTest() {
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(roomRequest)
                .when().post()
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Fail create new room (incorrect user input)")
    void saveWithIncorrectDataTest() {
        RoomRequest roomRequest = new RoomRequest("", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(roomRequest)
                .when().post()
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Room number is required"));
    }

    @Test
    @DisplayName("Find room by id")
    void findByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getRoomIdByNumber("101"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("maxCountOfGuests", equalTo(4));
    }

    @Test
    @DisplayName("Find room by number")
    void findByNumberTest() {
        given().contentType(ContentType.JSON)
                .pathParam("number", "101")
                .when().get("/number/{number}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("maxCountOfGuests", equalTo(4));
    }

    @Test
    @DisplayName("Successful search rooms")
    void searchTest() {
        given().contentType(ContentType.JSON)
                .params("types", "lux,standart single",
                        "minPrice", 1500, "guestsCount", 2)
                .when().get("/search")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(3));
    }

    @Test
    @DisplayName("Successful find free rooms")
    void findFreeRoomsTest() {
        FreeRoomRequest freeRoomRequest = new FreeRoomRequest(LocalDate.now(), LocalDate.now().plusDays(4));

        given().contentType(ContentType.JSON)
                .body(freeRoomRequest)
                .when().get("/freeRooms")
                .then()
                .statusCode(200)
                .assertThat()
                .body("content.size()", is(3));
    }

    @Test
    @DisplayName("Fail find free rooms")
    void findFreeRoomsWithIncorrectDataTest() {
        FreeRoomRequest freeRoomRequest = new FreeRoomRequest(LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(4));

        given().contentType(ContentType.JSON)
                .body(freeRoomRequest)
                .when().get("/freeRooms")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Check in date must be future or present"));
    }

    @Test
    @DisplayName("Successful update room")
    void updateTest() {
        RoomRequest roomRequest = new RoomRequest("104", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(roomRequest)
                .pathParam("id", testDBUtils.getRoomIdByNumber("101"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update new room type (incorrect user input)")
    void updateWithIncorrectDataTest() {
        RoomRequest roomRequest = new RoomRequest("", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .body(roomRequest)
                .pathParam("id", testDBUtils.getRoomIdByNumber("101"))
                .when().put("/{id}")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Room number is required"));
    }

    @Test
    @DisplayName("Delete room type")
    void deleteByIdTest() {
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + DBAuthentication.getToken())
                .pathParam("id", testDBUtils.getRoomIdByNumber("201"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}