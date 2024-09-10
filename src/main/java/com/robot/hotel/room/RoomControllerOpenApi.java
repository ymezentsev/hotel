package com.robot.hotel.room;

import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.room.dto.RoomRequest;
import com.robot.hotel.room.dto.RoomSearchParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Rooms Controller", description = "API to work with Rooms")
public interface RoomControllerOpenApi {

    @Operation(summary = "Get all rooms")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all rooms")
    })
    Page<RoomDto> findAll(Pageable pageable);

    @Operation(summary = "Create new room (for admins only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
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

    @Operation(summary = "Get all rooms filtered by types, min and max price, guests count")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched rooms"),
            @ApiResponse(
                    responseCode = "500",
                    description = "No property found for 'Room'")
    })
    Page<RoomDto> search(RoomSearchParameters parameters, Pageable pageable);

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
    Page<RoomDto> findFreeRooms(@Valid @RequestBody FreeRoomRequest freeRoomRequest, Pageable pageable);

    @Operation(summary = "Update room (for admins only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
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

    @Operation(summary = "Delete room (for admins only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
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