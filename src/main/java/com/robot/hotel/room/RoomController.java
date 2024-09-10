package com.robot.hotel.room;

import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.room.dto.RoomRequest;
import com.robot.hotel.room.dto.RoomSearchParameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController implements RoomControllerOpenApi {
    private final RoomService roomService;

    @GetMapping()
    public Page<RoomDto> findAll(Pageable pageable) {
        return roomService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @GetMapping("/search")
    public Page<RoomDto> search(RoomSearchParameters parameters, Pageable pageable) {
        return roomService.search(parameters, pageable);
    }

    @GetMapping("/freeRooms")
    public Page<RoomDto> findFreeRooms(@Valid @RequestBody FreeRoomRequest freeRoomRequest, Pageable pageable) {
        return roomService.findFreeRoomsPage(freeRoomRequest, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody RoomRequest roomRequest) {
        roomService.update(id, roomRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        roomService.deleteById(id);
    }
}