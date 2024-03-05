package com.robot.hotel.roomtype;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    private static final String TYPE_IS_ALREADY_EXISTS = "Such type of room is already exists";
    private static final String TYPE_IS_NOT_EXISTS = "Such type of room is not exists";
    private static final String ROOMS_OF_SUCH_TYPE_ARE_EXISTS = "There are rooms of this type at hotel. At first delete rooms";

    public RoomTypeDto save(RoomTypeRequest roomTypeRequest) {
        if (roomTypeRepository.existsByType(roomTypeRequest.getType().toLowerCase().strip())) {
            throw new DuplicateObjectException(TYPE_IS_ALREADY_EXISTS);
        }
        RoomType newRoomType = roomTypeMapper.buildRoomTypeFromRequest(roomTypeRequest);
        return roomTypeMapper.buildRoomTypeDto(roomTypeRepository.save(newRoomType));
    }

    public List<RoomTypeDto> findAll() {
        return roomTypeRepository.findAll().stream()
                .map(roomTypeMapper::buildRoomTypeDto)
                .toList();
    }

    public RoomTypeDto findByType(String type) {
        return roomTypeMapper.buildRoomTypeDto(roomTypeRepository
                .findByType(type.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS)));
    }

    public RoomTypeDto findById(Long id) {
        return roomTypeMapper.buildRoomTypeDto(roomTypeRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS)));
    }

    public void update(Long id, RoomTypeRequest roomTypeRequest) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(TYPE_IS_NOT_EXISTS)
        );

        if (roomTypeRepository.existsByType(roomTypeRequest.getType().toLowerCase().strip())) {
            throw new DuplicateObjectException(TYPE_IS_ALREADY_EXISTS);
        }

        roomType.setType(roomTypeRequest.getType().toLowerCase().strip());
        roomTypeRepository.save(roomType);
    }

    public void deleteById(Long id) {
        if (roomTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS))
                .getRooms()
                .isEmpty()) {
            roomTypeRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException(ROOMS_OF_SUCH_TYPE_ARE_EXISTS);
        }
    }
}