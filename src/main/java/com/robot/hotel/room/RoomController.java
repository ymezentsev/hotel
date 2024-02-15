package com.robot.hotel.room;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping()
    public List<RoomDto> findAll() {
        return roomService.findAll();
    }

    @PostMapping()
    public void save(@RequestBody RoomDto roomsDto) {
        roomService.save(roomsDto);
    }

    @GetMapping("/{id}")
    public Optional<RoomDto> findById(@PathVariable Long id) {
        return Optional.of(roomService.findById(id).orElseThrow());
    }

    @GetMapping("/number/{number}")
    public Optional<RoomDto> findByNumber(@PathVariable String number) {
        return Optional.of(roomService.findRoomsByNumber(number).orElseThrow());
    }

    @GetMapping("/type/{type}")
    public List<RoomDto> findByType(@PathVariable String type) {
        return roomService.findByType(type);
    }

    @GetMapping("/price>/{sum}")
    public List<RoomDto> findByPriceMoreThanOrEqual(@PathVariable BigDecimal sum) {
        return roomService.findByPriceMoreThanOrEqual(sum);
    }

    @GetMapping("/price</{sum}")
    public List<RoomDto> findByPriceLessThanOrEqual(@PathVariable BigDecimal sum) {
        return roomService.findByPriceLessThanOrEqual(sum);
    }

    @GetMapping("/guestsCount/{guestCount}")
    public List<RoomDto> findByGuestsCount(@PathVariable int guestCount) {
        return roomService.findByGuestsCount(guestCount);
    }

    @GetMapping("/available/{checkIn}/{checkOut}")
    public Set<RoomDto> findAvailableRooms(@PathVariable String checkIn, @PathVariable String checkOut) {
        return roomService.findAvailableRooms(checkIn, checkOut);
    }

    @PutMapping("/number/{number}")
    public void update(@PathVariable String number, @RequestBody RoomDto roomsDto) {
        roomService.update(number, roomsDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        roomService.deleteById(id);
    }
}
