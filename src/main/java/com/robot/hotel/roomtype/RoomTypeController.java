package com.robot.hotel.roomtype;

import com.robot.hotel.roomtype.dto.RoomTypeDto;
import com.robot.hotel.roomtype.dto.RoomTypeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roomTypes")
public class RoomTypeController implements RoomTypeControllerOpenApi {
    private final RoomTypeService roomTypeService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public RoomTypeDto save(@Valid @RequestBody RoomTypeRequest roomTypeRequest) {
        return roomTypeService.save(roomTypeRequest);
    }

    @GetMapping()
    public List<RoomTypeDto> findAll() {
        return roomTypeService.findAll();
    }

    @GetMapping("/type/{type}")
    public RoomTypeDto findByType(@PathVariable String type) {
        return roomTypeService.findByType(type);
    }

    @GetMapping("/{id}")
    public RoomTypeDto findById(@PathVariable Long id) {
        return roomTypeService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody RoomTypeRequest roomTypeRequest) {
        roomTypeService.update(id, roomTypeRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        roomTypeService.deleteById(id);
    }
}