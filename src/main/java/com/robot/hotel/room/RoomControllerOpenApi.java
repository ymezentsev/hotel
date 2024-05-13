package com.robot.hotel.room;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Tag(name = "Rooms Controller", description = "API to work with Rooms")
public interface RoomControllerOpenApi {

    @Operation(summary = "Get all rooms")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all rooms")
    })
    List<RoomDto> findAll();

    @Operation(summary = "Create new room")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Room successfully created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = RoomRequest.class))
                    }),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such room is already exists"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room type is not exists")
    })
    RoomDto save(@Valid @RequestBody RoomRequest roomRequest);

    @Operation(summary = "Get room by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched room"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists")
    })
    RoomDto findById(@PathVariable Long id);

    @Operation(summary = "Get room by number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched room"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists")
    })
    RoomDto findByNumber(@PathVariable String number);

    @Operation(summary = "Get rooms by type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched rooms"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room type is not exists")
    })
    List<RoomDto> findByType(@PathVariable String type);

    @Operation(summary = "Get rooms with more or equal price")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched rooms")
    })
    List<RoomDto> findByPriceMoreThanOrEqual(@PathVariable BigDecimal price);

    @Operation(summary = "Get rooms with less or equal price")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched rooms")
    })
    List<RoomDto> findByPriceLessThanOrEqual(@PathVariable BigDecimal price);

    @Operation(summary = "Get rooms with more or equal count of guests")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched rooms")
    })
    List<RoomDto> findByGuestsCount(@PathVariable int guestCount);

    @Operation(summary = "Get free rooms")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched rooms",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = FreeRoomRequest.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Check in date must be before check out date")
    })
    Set<RoomDto> findFreeRooms(@Valid @RequestBody FreeRoomRequest freeRoomRequest);

    @Operation(summary = "Update room")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated room",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = RoomRequest.class))
                    }),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such room is already exists"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room type is not exists")
    })
    void update(@PathVariable Long id, @Valid @RequestBody RoomRequest roomRequest);

    @Operation(summary = "Delete room")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted room"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Failed to delete room. " +
                            "There are reservations for this room. At first delete reservations"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists")
    })
    void deleteById(@PathVariable Long id);
}