package com.robot.hotel.roomtype;

import com.robot.hotel.room.Room;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomType save(RoomTypeDto roomTypeDto) {
        if (findByType(roomTypeDto.getType()).isPresent()) {
            throw new DuplicateObjectException("Such type of room is already exists");
        } else {
            RoomType roomType = buildRoomType(roomTypeDto);
            return roomTypeRepository.save(roomType);
        }
    }

    private RoomType buildRoomType(RoomTypeDto roomTypeDto) {
        return RoomType.builder()
                .type(roomTypeDto.getType().toLowerCase())
                .build();
    }

    public List<RoomTypeDto> findAll() {
        return roomTypeRepository.findAll().stream()
                .map(RoomTypeService::buildRoomTypeDto)
                .collect(Collectors.toList());
    }

    private static RoomTypeDto buildRoomTypeDto(RoomType roomType) {
        return RoomTypeDto.builder()
                .id(roomType.getId())
                .type(roomType.getType())
                .rooms(roomType.getRooms().stream()
                        .map(Room::getNumber)
                        .collect(Collectors.toList()))
                .build();
    }

    public Optional<RoomTypeDto> findByType(String type) {
        return roomTypeRepository.findRoomTypeByType(type.toLowerCase()).map(RoomTypeService::buildRoomTypeDto);
    }

    public Optional<RoomTypeDto> findById(Long id) {
        return roomTypeRepository.findById(id).map(RoomTypeService::buildRoomTypeDto);
    }

    public void update(Long id, RoomTypeDto roomTypeDto) {
        if (roomTypeRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Type of room with id " + id + " is not exists");
        }

        if (findByType(roomTypeDto.getType()).isPresent()) {
            throw new DuplicateObjectException("Such type of room is already exists");
        }

        RoomType roomType = roomTypeRepository.findById(id).get();
        roomType.setType(roomTypeDto.getType().toLowerCase());
        roomTypeRepository.save(roomType);
    }

    public void deleteById(Long id) {
        if (roomTypeRepository.findById(id).orElseThrow().getRooms().isEmpty()) {
            roomTypeRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException("There are rooms of this type at hotel. At first delete rooms.");
        }
    }
}
