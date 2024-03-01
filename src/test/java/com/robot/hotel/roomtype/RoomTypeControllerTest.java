package com.robot.hotel.roomtype;

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
class RoomTypeControllerTest {
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
        RestAssured.baseURI = "http://localhost:" + port + "/roomTypes";
    }

    @Test
    @DisplayName("Successful create new room type")
    void saveTest() {
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("new lux");

        given().contentType(ContentType.JSON)
                .body(roomTypeRequest)
                .when().post()
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Fail create new room type (incorrect user input)")
    void saveWithIncorrectDataTest() {
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("  ");

        given().contentType(ContentType.JSON)
                .body(roomTypeRequest)
                .when().post()
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Room type is required"));
    }

    @Test
    @DisplayName("Find all room types")
    void findAllTest() {
        given().contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(4));
    }

    @Test
    @DisplayName("Find room type by type")
    void findByTypeTest() {
        given().contentType(ContentType.JSON)
                .pathParam("type", "lux")
                .when().get("/type/{type}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Find room type by id")
    void findByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getRoomTypeIdByType("lux"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("type", equalTo("lux"));
    }

    @Test
    @DisplayName("Successful update room type")
    void updateTest() {
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("new lux");

        given().contentType(ContentType.JSON)
                .body(roomTypeRequest)
                .pathParam("id", testDBUtils.getRoomTypeIdByType("lux"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update new room type (incorrect user input)")
    void updateWithIncorrectDataTest() {
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("  ");

        given().contentType(ContentType.JSON)
                .body(roomTypeRequest)
                .pathParam("id", testDBUtils.getRoomTypeIdByType("lux"))
                .when().put("/{id}")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Room type is required"));
    }

    @Test
    @DisplayName("Delete room type")
    void deleteByIdTest() {
        given().contentType(ContentType.JSON)
                .pathParam("id", testDBUtils.getRoomTypeIdByType("king"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}