package com.robot.hotel.rest;

import com.robot.hotel.dto.RoomTypeDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomTypes")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody RoomTypeDto roomTypeDto) {
        try {
            roomTypeService.save(roomTypeDto);
        } catch (DublicateObjectException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<RoomTypeDto>> findAll() {
        return ResponseEntity.ok(roomTypeService.findAll());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<RoomTypeDto> findByType(@PathVariable String type) {
        return roomTypeService.findByType(type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDto> findById(@PathVariable Long id) {
        return roomTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody RoomTypeDto roomTypeDto) {
        try {
            roomTypeService.update(id, roomTypeDto);
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (DublicateObjectException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            roomTypeService.deleteById(id);
        } catch (NotEmptyObjectException | NoSuchElementException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}