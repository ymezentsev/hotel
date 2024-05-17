package com.robot.hotel.reservation;

import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationRequest;
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

import java.util.List;

@Tag(name = "Reservations Controller", description = "API to work with Reservations")
public interface ReservationControllerOpenApi {
    @Operation(summary = "Get all reservations")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all reservations")
    })
    List<ReservationDto> findAll();

    @Operation(summary = "Get reservation by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched reservation"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such reservation is not exists")
    })
    ReservationDto findById(@PathVariable Long id);

    @Operation(summary = "Create new reservation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reservation successfully created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = ReservationRequest.class))
                    }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such user is not exists"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Check in date must be before check out date"),
            @ApiResponse(
                    responseCode = "400",
                    description = "You can't reserve room for more than 60 days"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Reservation of rooms opens 180 days in advance"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The quantity of guests exceeds the maximum allowed in this room"),
            @ApiResponse(
                    responseCode = "400",
                    description = "This room is occupied for your dates")
    })
    ReservationDto save(@Valid @RequestBody ReservationRequest reservationRequest);

    @Operation(summary = "Get reservations by user id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched reservations by user id")
    })
    List<ReservationDto> findReservationsByUserId(@PathVariable Long id);

    @Operation(summary = "Get reservations by room's number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched reservations by room's number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists")
    })
    List<ReservationDto> findReservationsByRoom(@PathVariable String roomNumber);

    @Operation(summary = "Get all current reservations")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all current reservations")
    })
    List<ReservationDto> findCurrentReservations();

    @Operation(summary = "Get all current reservations for a specific room")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all current reservations for a specific room"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists")
    })
    List<ReservationDto> findCurrentReservationsForSpecificRoom(@PathVariable String roomNumber);

    @Operation(summary = "Delete reservation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted reservation"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such reservation is not exists")
    })
    void deleteById(@PathVariable Long id);
}