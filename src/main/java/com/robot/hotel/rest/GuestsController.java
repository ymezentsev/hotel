package com.robot.hotel.rest;

import com.robot.hotel.dto.GuestsDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.service.GuestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestsController {

    private final GuestsService guestsService;

    @GetMapping()
    public ResponseEntity<List<GuestsDto>> findAll() {
        return ResponseEntity.ok(guestsService.findAll());
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<List<GuestsDto>> findReservation(@PathVariable Long id) {
        return ResponseEntity.ok(guestsService.findReservation(id));
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody GuestsDto guestsDto) {
        try {
            guestsService.save(guestsDto);
        } catch (DublicateObjectException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestsDto> findById(@PathVariable Long id) {
        return guestsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GuestsDto> findByEmail(@PathVariable String email) {
        return guestsService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telNumber/{telNumber}")
    public ResponseEntity<GuestsDto> findByTelNumber(@PathVariable String telNumber) {
        return guestsService.findByTelNumber(telNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/passport/{passportSerialNumber}")
    public ResponseEntity<GuestsDto> findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return guestsService.findByPassportSerialNumber(passportSerialNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<GuestsDto>> findByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(guestsService.findByLastName(lastName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody GuestsDto guestsDto) {
        try {
            guestsService.update(id, guestsDto);
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
            guestsService.deleteById(id);
        } catch (NotEmptyObjectException | NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
