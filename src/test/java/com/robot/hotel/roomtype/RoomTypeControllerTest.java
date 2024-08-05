package com.robot.hotel.roomtype;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.TestDBAuthentication;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.roomtype.dto.RoomTypeRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,  classes = ContainerConfiguration.class)
class RoomTypeControllerTest {
    @LocalServerPort
    Integer port;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @Autowired
    TestDBAuthentication testDBAuthentication;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        testDBAuthentication.loginUser();
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/roomTypes";
    }

    @Test
    @DisplayName("Successful create new room type")
    void saveTest() {
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("new lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
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
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
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
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
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
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
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
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
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
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
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
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
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
                .header("Authorization", "Bearer " + testDBAuthentication.getToken())
                .pathParam("id", testDBUtils.getRoomTypeIdByType("king"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}