package com.robot.hotel.rest;

import com.robot.hotel.dto.GuestDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    @GetMapping()
    public ResponseEntity<List<GuestDto>> findAll() {
        return ResponseEntity.ok(guestService.findAll());
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<List<GuestDto>> findGuestByReservation(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.findGuestByReservation(id));
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody GuestDto guestsDto) {
        try {
            guestService.save(guestsDto);
        } catch (DublicateObjectException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDto> findById(@PathVariable Long id) {
        return guestService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GuestDto> findByEmail(@PathVariable String email) {
        return guestService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telNumber/{telNumber}")
    public ResponseEntity<GuestDto> findByTelNumber(@PathVariable String telNumber) {
        return guestService.findByTelNumber(telNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/passport/{passportSerialNumber}")
    public ResponseEntity<GuestDto> findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return guestService.findByPassportSerialNumber(passportSerialNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<GuestDto>> findByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(guestService.findByLastName(lastName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody GuestDto guestsDto) {
        try {
            guestService.update(id, guestsDto);
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (DublicateObjectException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            guestService.deleteById(id);
        } catch (NotEmptyObjectException | NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
