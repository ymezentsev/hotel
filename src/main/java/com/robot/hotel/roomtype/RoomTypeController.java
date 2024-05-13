package com.robot.hotel.roomtype;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomTypes")
public class RoomTypeController implements RoomTypeControllerOpenApi {
    private final RoomTypeService roomTypeService;

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

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody RoomTypeRequest roomTypeRequest) {
        roomTypeService.update(id, roomTypeRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        roomTypeService.deleteById(id);
    }
}