package com.robot.hotel.rest;

import com.robot.hotel.dto.RoomTypeDto;
import com.robot.hotel.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomTypes")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping()
    public void save(@RequestBody RoomTypeDto roomTypeDto) {
        roomTypeService.save(roomTypeDto);
    }

    @GetMapping()
    public List<RoomTypeDto> findAll() {
        return roomTypeService.findAll();
    }

    @GetMapping("/type/{type}")
    public Optional<RoomTypeDto> findByType(@PathVariable String type) {
        return Optional.of(roomTypeService.findByType(type).orElseThrow());
    }

    @GetMapping("/{id}")
    public Optional<RoomTypeDto> findById(@PathVariable Long id) {
        return Optional.of(roomTypeService.findById(id).orElseThrow());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody RoomTypeDto roomTypeDto) {
        roomTypeService.update(id, roomTypeDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        roomTypeService.deleteById(id);
    }
}