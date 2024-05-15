package com.robot.hotel.room;

import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.room.dto.RoomRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController implements RoomControllerOpenApi {

    private final RoomService roomService;

    @GetMapping()
    public List<RoomDto> findAll() {
        return roomService.findAll();
    }

    @PostMapping()
    public RoomDto save(@Valid @RequestBody RoomRequest roomRequest) {
        return roomService.save(roomRequest);
    }

    @GetMapping("/{id}")
    public RoomDto findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @GetMapping("/number/{number}")
    public RoomDto findByNumber(@PathVariable String number) {
        return roomService.findByNumber(number);
    }

    @GetMapping("/type/{type}")
    public List<RoomDto> findByType(@PathVariable String type) {
        return roomService.findByType(type);
    }

    @GetMapping("/price>/{price}")
    public List<RoomDto> findByPriceMoreThanOrEqual(@PathVariable BigDecimal price) {
        return roomService.findByPriceMoreThanOrEqual(price);
    }

    @GetMapping("/price</{price}")
    public List<RoomDto> findByPriceLessThanOrEqual(@PathVariable BigDecimal price) {
        return roomService.findByPriceLessThanOrEqual(price);
    }

    @GetMapping("/guestsCount/{guestCount}")
    public List<RoomDto> findByGuestsCount(@PathVariable int guestCount) {
        return roomService.findByGuestsCount(guestCount);
    }

    @GetMapping("/freeRooms")
    public Set<RoomDto> findFreeRooms(@Valid @RequestBody FreeRoomRequest freeRoomRequest) {
        return roomService.findFreeRooms(freeRoomRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody RoomRequest roomRequest) {
        roomService.update(id, roomRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        roomService.deleteById(id);
    }
}