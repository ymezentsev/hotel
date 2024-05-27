package com.robot.hotel.reservation;

import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController implements ReservationControllerOpenApi {
    private final ReservationService reservationService;

    @GetMapping()
    public Page<ReservationDto> findAll(Pageable pageable) {
        return reservationService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ReservationDto findById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping()
    public ReservationDto save(@Valid @RequestBody ReservationRequest reservationRequest) {
        return reservationService.save(reservationRequest);
    }

    @GetMapping("/user/{id}")
    public List<ReservationDto> findReservationsByUserId(@PathVariable Long id) {
        return reservationService.findReservationsByUserId(id);
    }

    @GetMapping("/room/{roomNumber}")
    public List<ReservationDto> findReservationsByRoom(@PathVariable String roomNumber) {
        return reservationService.findReservationsByRoom(roomNumber);
    }

    @GetMapping("/current")
    public List<ReservationDto> findCurrentReservations() {
        return reservationService.findCurrentReservations();
    }

    @GetMapping("/current/room/{roomNumber}")
    public List<ReservationDto> findCurrentReservationsForSpecificRoom(@PathVariable String roomNumber) {
        return reservationService.findCurrentReservationsForSpecificRoom(roomNumber);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}