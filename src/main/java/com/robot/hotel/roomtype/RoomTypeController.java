package com.robot.hotel.roomtype;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomTypes")
@Tag(name = "RoomTypes", description = "API to work with RoomTypes")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping()
    @Operation(summary = "Create new room type")
    public void save(@RequestBody RoomTypeDto roomTypeDto) {
        roomTypeService.save(roomTypeDto);
    }

    @GetMapping()
    @Operation(summary = "Get all room types")
    public List<RoomTypeDto> findAll() {
        return roomTypeService.findAll();
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get room type by type")
    public Optional<RoomTypeDto> findByType(@PathVariable String type) {
        return Optional.of(roomTypeService.findByType(type).orElseThrow());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room type by id")
    public Optional<RoomTypeDto> findById(@PathVariable Long id) {
        return Optional.of(roomTypeService.findById(id).orElseThrow());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room type")
    public void update(@PathVariable Long id, @RequestBody RoomTypeDto roomTypeDto) {
        roomTypeService.update(id, roomTypeDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room type")
    public void deleteById(@PathVariable Long id) {
        roomTypeService.deleteById(id);
    }
}
