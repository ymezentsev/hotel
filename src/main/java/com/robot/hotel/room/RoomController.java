package com.robot.hotel.room;

import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.room.dto.RoomRequest;
import com.robot.hotel.room.dto.RoomSearchParameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController implements RoomControllerOpenApi {
    private final RoomService roomService;

    @GetMapping()
    public Page<RoomDto> findAll(Pageable pageable) {
        return roomService.findAll(pageable);
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
    public Page<RoomDto> findByType(@PathVariable String type, Pageable pageable) {
        return roomService.findByType(type, pageable);
    }

    @GetMapping("/price>/{price}")
    public Page<RoomDto> findByPriceMoreThanOrEqual(@PathVariable BigDecimal price, Pageable pageable) {
        return roomService.findByPriceMoreThanOrEqual(price, pageable);
    }

    @GetMapping("/price</{price}")
    public Page<RoomDto> findByPriceLessThanOrEqual(@PathVariable BigDecimal price, Pageable pageable) {
        return roomService.findByPriceLessThanOrEqual(price, pageable);
    }

    @GetMapping("/guestsCount/{guestCount}")
    public Page<RoomDto> findByGuestsCount(@PathVariable int guestCount, Pageable pageable) {
        return roomService.findByGuestsCount(guestCount, pageable);
    }

    @GetMapping("/search")
    public Page<RoomDto> search(RoomSearchParameters parameters, Pageable pageable) {
        return roomService.search(parameters, pageable);
    }

    @GetMapping("/freeRooms")
    public Page<RoomDto> findFreeRooms(@Valid @RequestBody FreeRoomRequest freeRoomRequest, Pageable pageable) {
        return roomService.findFreeRoomsPage(freeRoomRequest, pageable);
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