package com.robot.hotel.reservation;

import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationRequest;
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

@Tag(name = "Reservations Controller", description = "API to work with Reservations")
public interface ReservationControllerOpenApi {
    @Operation(summary = "Get all reservations (for admins and managers only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all reservations")
    })
    Page<ReservationDto> findAll(Pageable pageable);

    @Operation(summary = "Get reservation by id (for admins and managers only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched reservation"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such reservation is not exists")
    })
    ReservationDto findById(@PathVariable Long id);

    @Operation(summary = "Create new reservation",
            security = @SecurityRequirement(name = "Bearer Authentication"))
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

    @Operation(summary = "Get reservations by user id (for admins and managers only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched reservations by user id")
    })
    Page<ReservationDto> findReservationsByUserId(@PathVariable Long id, Pageable pageable);

    @Operation(summary = "Get reservations by room's number (for admins and managers only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched reservations by room's number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists")
    })
    Page<ReservationDto> findReservationsByRoom(@PathVariable String roomNumber, Pageable pageable);

    @Operation(summary = "Get all current reservations (for admins and managers only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all current reservations")
    })
    Page<ReservationDto> findCurrentReservations(Pageable pageable);

    @Operation(summary = "Get all current reservations for a specific room (for admins and managers only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all current reservations for a specific room"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such room is not exists")
    })
    Page<ReservationDto> findCurrentReservationsForSpecificRoom(@PathVariable String roomNumber, Pageable pageable);

    @Operation(summary = "Delete reservation (for admins, managers and reservation's owner only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
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