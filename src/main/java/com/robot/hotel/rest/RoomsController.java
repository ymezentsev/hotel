package com.robot.hotel.rest;

import com.robot.hotel.dto.RoomsDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.service.RoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomsController {

    private final RoomsService roomsService;

    @GetMapping()
    public ResponseEntity<List<RoomsDto>> findAll() {
        return ResponseEntity.ok(roomsService.findAll());
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody RoomsDto roomsDto) {
        try {
            roomsService.save(roomsDto);
        } catch (DublicateObjectException | NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomsDto> findById(@PathVariable Long id) {
        return roomsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<RoomsDto> findByNumber(@PathVariable String number) {
        return roomsService.findRoomsByNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<RoomsDto>> findByType(@PathVariable String type) {
        try {
            return ResponseEntity.ok(roomsService.findByType(type));
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/price>/{sum}")
    public ResponseEntity<List<RoomsDto>> findByPriceMoreThan(@PathVariable BigDecimal sum) {
        return ResponseEntity.ok(roomsService.findByPriceMoreThan(sum));
    }

    @GetMapping("/price</{sum}")
    public ResponseEntity<List<RoomsDto>> findByPriceLessThan(@PathVariable BigDecimal sum) {
        return ResponseEntity.ok(roomsService.findByPriceLessThan(sum));
    }

    @GetMapping("/guestsCount/{guestCount}")
    public ResponseEntity<List<RoomsDto>> findByGuestsCount(@PathVariable int guestCount) {
        return ResponseEntity.ok(roomsService.findByGuestsCount(guestCount));
    }
    @GetMapping("/available/{checkIn}/{checkOut}")
    public ResponseEntity<Set<RoomsDto>> findAvailableRooms(@PathVariable String checkIn, @PathVariable String checkOut) {
        try {
            return ResponseEntity.ok(roomsService.findAvailableRooms(checkIn, checkOut));
        } catch (WrongDatesException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }

    @PutMapping("/number/{number}")
    public ResponseEntity<Void> update(@PathVariable String number, @RequestBody RoomsDto roomsDto) {
        try {
            roomsService.update(number, roomsDto);
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            roomsService.deleteById(id);
        } catch (NotEmptyObjectException | NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}