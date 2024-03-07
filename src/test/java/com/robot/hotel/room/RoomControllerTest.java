package com.robot.hotel.room;

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

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,  classes = ContainerConfiguration.class)
class RoomControllerTest {
/*    @Container
    @ServiceConnection
    static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0");*/

    @LocalServerPort
    private Integer port;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    private TestDBUtils testDBUtils;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        RestAssured.baseURI = "http://localhost:" + port + "/rooms";
    }

    @Test
    @DisplayName("Find all rooms")
    void findAllTest() {
        given().contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(5));
    }

    @Test
    @DisplayName("Successful create new room")
    void saveTest() {
        RoomRequest roomRequest = new RoomRequest("105", BigDecimal.valueOf(1000), 3, "lux");

        given().contentType(ContentType.JSON)
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
    @DisplayName("Find rooms by type")
    void findByTypeTest() {
        given().contentType(ContentType.JSON)
                .pathParam("type", "standart single")
                .when().get("/type/{type}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(2));
    }

    @Test
    @DisplayName("Find rooms with price more or equal than given")
    void findByPriceMoreThanOrEqualTest() {
        given().contentType(ContentType.JSON)
                .pathParam("price", 1500)
                .when().get("price>/{price}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(3));
    }

    @Test
    @DisplayName("Find rooms with price less or equal than given")
    void findByPriceLessThanOrEqualTest() {
        given().contentType(ContentType.JSON)
                .pathParam("price", 1500)
                .when().get("/price</{price}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(4));
    }

    @Test
    @DisplayName("Find rooms with max count of guests more or equal than given")
    void findByGuestsCountTest() {
        given().contentType(ContentType.JSON)
                .pathParam("guestCount", 3)
                .when().get("/guestsCount/{guestCount}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(1));
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
                .body("size()", is(3));
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
                .pathParam("id", testDBUtils.getRoomIdByNumber("201"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}