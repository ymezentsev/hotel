package com.robot.hotel.roomtype;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBAuthentication;
import com.robot.hotel.DBUtils;
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
    DBUtils dbUtils;

    @Autowired
    DBAuthentication dbAuthentication;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
        RestAssured.baseURI = "http://localhost:" + port + "/api/v1/roomTypes";
    }

    @Test
    @DisplayName("Successful create new room type")
    void saveTest() {
        dbAuthentication.loginAdmin();
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("new lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginAdmin();
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("  ");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginUser();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .when().get()
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", is(4));
    }

    @Test
    @DisplayName("Find room type by type")
    void findByTypeTest() {
        dbAuthentication.loginUser();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
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
        dbAuthentication.loginUser();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", dbUtils.getRoomTypeIdByType("lux"))
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .assertThat()
                .body("type", equalTo("lux"));
    }

    @Test
    @DisplayName("Successful update room type")
    void updateTest() {
        dbAuthentication.loginAdmin();
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("new lux");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .body(roomTypeRequest)
                .pathParam("id", dbUtils.getRoomTypeIdByType("lux"))
                .when().put("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Fail update new room type (incorrect user input)")
    void updateWithIncorrectDataTest() {
        dbAuthentication.loginAdmin();
        RoomTypeRequest roomTypeRequest = new RoomTypeRequest("  ");

        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .body(roomTypeRequest)
                .pathParam("id", dbUtils.getRoomTypeIdByType("lux"))
                .when().put("/{id}")
                .then()
                .statusCode(400)
                .assertThat()
                .body(containsString("Room type is required"));
    }

    @Test
    @DisplayName("Delete room type")
    void deleteByIdTest() {
        dbAuthentication.loginAdmin();
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + dbAuthentication.getToken())
                .pathParam("id", dbUtils.getRoomTypeIdByType("king"))
                .when().delete("/{id}")
                .then()
                .statusCode(200);
    }
}