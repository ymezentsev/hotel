package com.robot.hotel.rest;

import com.robot.hotel.dto.RoomDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.service.RoomService;
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
public class RoomController {

    private final RoomService roomService;

    @GetMapping()
    public ResponseEntity<List<RoomDto>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody RoomDto roomsDto) {
        try {
            roomService.save(roomsDto);
        } catch (DublicateObjectException | NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> findById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<RoomDto> findByNumber(@PathVariable String number) {
        return roomService.findRoomsByNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<RoomDto>> findByType(@PathVariable String type) {
        try {
            return ResponseEntity.ok(roomService.findByType(type));
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/price>/{sum}")
    public ResponseEntity<List<RoomDto>> findByPriceMoreThan(@PathVariable BigDecimal sum) {
        return ResponseEntity.ok(roomService.findByPriceMoreThan(sum));
    }

    @GetMapping("/price</{sum}")
    public ResponseEntity<List<RoomDto>> findByPriceLessThan(@PathVariable BigDecimal sum) {
        return ResponseEntity.ok(roomService.findByPriceLessThan(sum));
    }

    @GetMapping("/guestsCount/{guestCount}")
    public ResponseEntity<List<RoomDto>> findByGuestsCount(@PathVariable int guestCount) {
        return ResponseEntity.ok(roomService.findByGuestsCount(guestCount));
    }
    @GetMapping("/available/{checkIn}/{checkOut}")
    public ResponseEntity<Set<RoomDto>> findAvailableRooms(@PathVariable String checkIn, @PathVariable String checkOut) {
        try {
            return ResponseEntity.ok(roomService.findAvailableRooms(checkIn, checkOut));
        } catch (WrongDatesException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }

    @PutMapping("/number/{number}")
    public ResponseEntity<Void> update(@PathVariable String number, @RequestBody RoomDto roomsDto) {
        try {
            roomService.update(number, roomsDto);
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            roomService.deleteById(id);
        } catch (NotEmptyObjectException | NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}