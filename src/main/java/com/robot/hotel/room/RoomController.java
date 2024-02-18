package com.robot.hotel.room;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Tag(name = "Rooms", description = "API to work with Rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping()
    @Operation(summary = "Get all rooms")
    public List<RoomDto> findAll() {
        return roomService.findAll();
    }

    @PostMapping()
    @Operation(summary = "Create new room")
    public RoomDto save(@Valid @RequestBody RoomRequest roomRequest) {
        return roomService.save(roomRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room by id")
    public RoomDto findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @GetMapping("/number/{number}")
    @Operation(summary = "Get room by number")
    public RoomDto findByNumber(@PathVariable String number) {
        return roomService.findByNumber(number);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get rooms by type")
    public List<RoomDto> findByType(@PathVariable String type) {
        return roomService.findByType(type);
    }

    @GetMapping("/price>/{price}")
    @Operation(summary = "Get rooms with more or equal price")
    public List<RoomDto> findByPriceMoreThanOrEqual(@PathVariable BigDecimal price) {
        return roomService.findByPriceMoreThanOrEqual(price);
    }

    @GetMapping("/price</{price}")
    @Operation(summary = "Get rooms with less or equal price")
    public List<RoomDto> findByPriceLessThanOrEqual(@PathVariable BigDecimal price) {
        return roomService.findByPriceLessThanOrEqual(price);
    }

    @GetMapping("/guestsCount/{guestCount}")
    @Operation(summary = "Get rooms with less or equal count of guests")
    public List<RoomDto> findByGuestsCount(@PathVariable int guestCount) {
        return roomService.findByGuestsCount(guestCount);
    }

    @GetMapping("/available/{checkIn}/{checkOut}")
    @Operation(summary = "Get available rooms")
    public Set<RoomDto> findAvailableRooms(@PathVariable String checkIn, @PathVariable String checkOut) {
        return roomService.findAvailableRooms(checkIn, checkOut);
    }

    @PutMapping("/number/{number}")
    @Operation(summary = "Update room")
    public void update(@PathVariable String number, @RequestBody RoomDto roomsDto) {
        roomService.update(number, roomsDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room")
    public void deleteById(@PathVariable Long id) {
        roomService.deleteById(id);
    }
}
