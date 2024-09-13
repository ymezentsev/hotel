package com.robot.hotel.room;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBAuthentication;
import com.robot.hotel.DBUtils;
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
    DBUtils DBUtils;

    @Autowired
    DBAuthentication dbAuthentication;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/rooms";
    }

    @Test
    @DisplayName("Find all rooms")
    void findAllTest() {
        dbAuthentication.loginUser();
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
        dbAuthentication.loginAdmin();
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginAdmin();
        RoomRequest roomRequest = new RoomRequest("", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginUser();
        given().contentType(ContentType.JSON)
                .pathParam("id", DBUtils.getRoomIdByNumber("101"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("maxCountOfGuests", equalTo(4));
    }

    @Test
    @DisplayName("Find room by number")
    void findByNumberTest() {
        dbAuthentication.loginUser();
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
        dbAuthentication.loginUser();
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
        dbAuthentication.loginUser();
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
        dbAuthentication.loginUser();
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
        dbAuthentication.loginAdmin();
        RoomRequest roomRequest = new RoomRequest("104", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .body(roomRequest)
                .pathParam("id", DBUtils.getRoomIdByNumber("101"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update new room type (incorrect user input)")
    void updateWithIncorrectDataTest() {
        dbAuthentication.loginAdmin();
        RoomRequest roomRequest = new RoomRequest("", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .body(roomRequest)
                .pathParam("id", DBUtils.getRoomIdByNumber("101"))
                .when().put("/{id}")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Room number is required"));
    }

    @Test
    @DisplayName("Delete room type")
    void deleteByIdTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", DBUtils.getRoomIdByNumber("201"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}