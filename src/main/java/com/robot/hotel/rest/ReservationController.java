package com.robot.hotel.rest;

import com.robot.hotel.dto.ReservationsDto;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping()
    public ResponseEntity<List<ReservationsDto>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationsDto> findById(@PathVariable Long id) {
        return reservationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody ReservationsDto reservationsDto) {
        try {
            reservationService.save(reservationsDto);
        } catch (NoSuchElementException | GuestsQuantityException | WrongDatesException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/guest/{id}")
    public ResponseEntity<List<ReservationsDto>> findReservationsByGuestsId(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findReservationsByGuestsId(id));
    }

    @GetMapping("/room/{roomNumber}")
    public ResponseEntity<List<ReservationsDto>> findReservationsByRoom(@PathVariable String roomNumber) {
        try {
            return ResponseEntity.ok(reservationService.findReservationsByRoom(roomNumber));
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/current")
    public ResponseEntity<List<ReservationsDto>> findCurrentReservations() {
        return ResponseEntity.ok(reservationService.findCurrentReservations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            reservationService.deleteById(id);
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
