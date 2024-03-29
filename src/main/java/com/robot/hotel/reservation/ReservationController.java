package com.robot.hotel.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "Reservations", description = "API to work with Reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping()
    @Operation(summary = "Get all reservations")
    public List<ReservationDto> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by id")
    public ReservationDto findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping()
    @Operation(summary = "Create new reservation")
    public ReservationDto save(@Valid @RequestBody ReservationRequest reservationRequest) {
        return reservationService.save(reservationRequest);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get reservations by user id")
    public List<ReservationDto> findReservationsByUserId(@PathVariable Long id) {
        return reservationService.findReservationsByUserId(id);
    }

    @GetMapping("/room/{roomNumber}")
    @Operation(summary = "Get reservations by room's number")
    public List<ReservationDto> findReservationsByRoom(@PathVariable String roomNumber) {
        return reservationService.findReservationsByRoom(roomNumber);
    }

    @GetMapping("/current")
    @Operation(summary = "Get all current reservations")
    public List<ReservationDto> findCurrentReservations() {
        return reservationService.findCurrentReservations();
    }

    @GetMapping("/current/room/{roomNumber}")
    @Operation(summary = "Get all current reservations for a specific room")
    public List<ReservationDto> findCurrentReservationsForSpecificRoom(@PathVariable String roomNumber) {
        return reservationService.findCurrentReservationsForSpecificRoom(roomNumber);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reservation")
    public void deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
