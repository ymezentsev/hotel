package com.robot.hotel.reservation;

import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController implements ReservationControllerOpenApi {
    private final ReservationService reservationService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping()
    public Page<ReservationDto> findAll(Pageable pageable) {
        return reservationService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping("/{id}")
    public ReservationDto findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping()
    public ReservationDto save(@Valid @RequestBody ReservationRequest reservationRequest) {
        return reservationService.save(reservationRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping("/user/{id}")
    public Page<ReservationDto> findReservationsByUserId(@PathVariable Long id, Pageable pageable) {
        return reservationService.findReservationsByUserId(id, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping("/room/{roomNumber}")
    public Page<ReservationDto> findReservationsByRoom(@PathVariable String roomNumber, Pageable pageable) {
        return reservationService.findReservationsByRoom(roomNumber, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping("/current")
    public Page<ReservationDto> findCurrentReservations(Pageable pageable) {
        return reservationService.findCurrentReservations(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping("/current/room/{roomNumber}")
    public Page<ReservationDto> findCurrentReservationsForSpecificRoom(@PathVariable String roomNumber,
                                                                       Pageable pageable) {
        return reservationService.findCurrentReservationsForSpecificRoom(roomNumber, pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}