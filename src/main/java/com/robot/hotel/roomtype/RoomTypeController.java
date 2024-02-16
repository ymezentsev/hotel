package com.robot.hotel.roomtype;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomTypes")
@Tag(name = "RoomTypes", description = "API to work with RoomTypes")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping()
    @Operation(summary = "Create new room type")
    public RoomTypeDto save(@Valid @RequestBody RoomTypeRequest roomTypeRequest) {
        return roomTypeService.save(roomTypeRequest);
    }

    @GetMapping()
    @Operation(summary = "Get all room types")
    public List<RoomTypeDto> findAll() {
        return roomTypeService.findAll();
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get room type by type")
    public RoomTypeDto findByType(@PathVariable String type) {
        return roomTypeService.findByType(type);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room type by id")
    public RoomTypeDto findById(@PathVariable Long id) {
        return roomTypeService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room type")
    public void update(@PathVariable Long id, @Valid @RequestBody RoomTypeRequest roomTypeRequest) {
        roomTypeService.update(id, roomTypeRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room type")
    public void deleteById(@PathVariable Long id) {
        roomTypeService.deleteById(id);
    }
}
