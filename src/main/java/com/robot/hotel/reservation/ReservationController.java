package com.robot.hotel.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping()
    public List<ReservationDto> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ReservationDto> findById(@PathVariable Long id) {
        return Optional.of(reservationService.findById(id).orElseThrow());
    }

    @PostMapping()
    public void save(@RequestBody ReservationDto reservationsDto) {
        reservationService.save(reservationsDto);
    }

    @GetMapping("/guest/{id}")
    public List<ReservationDto> findReservationsByGuestsId(@PathVariable Long id) {
        return reservationService.findReservationsByGuestsId(id);
    }

    @GetMapping("/room/{roomNumber}")
    public List<ReservationDto> findReservationsByRoom(@PathVariable String roomNumber) {
        return reservationService.findReservationsByRoom(roomNumber);
    }

    @GetMapping("/current")
    public List<ReservationDto> findCurrentReservations() {
        return reservationService.findCurrentReservations();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
