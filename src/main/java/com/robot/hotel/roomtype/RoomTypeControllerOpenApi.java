package com.robot.hotel.roomtype;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RoomTypes Controller", description = "API to work with RoomTypes")
public interface RoomTypeControllerOpenApi {
    @Operation(summary = "Create new room type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Room type successfully created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = RoomTypeRequest.class))
                    }),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such room type is already exists")
    })
    RoomTypeDto save(@Valid @RequestBody RoomTypeRequest roomTypeRequest);

    @Operation(summary = "Get all room types")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all room types")
    })
    List<RoomTypeDto> findAll();

    @Operation(summary = "Get room type by type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched room type"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room type is not exists")
    })
    RoomTypeDto findByType(@PathVariable String type);

    @Operation(summary = "Get room type by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched room type"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room type is not exists")
    })
    RoomTypeDto findById(@PathVariable Long id);

    @Operation(summary = "Update room type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated room type",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = RoomTypeRequest.class))
                    }),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such room type is already exists"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room type is not exists")
    })
    void update(@PathVariable Long id, @Valid @RequestBody RoomTypeRequest roomTypeRequest);

    @Operation(summary = "Delete room type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted room type"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Failed to delete room type. " +
                            "There are rooms of this type at hotel. At first delete room"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room type is not exists")
    })
    void deleteById(@PathVariable Long id);
}