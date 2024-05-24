package com.robot.hotel.roomtype;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.roomtype.dto.RoomTypeDto;
import com.robot.hotel.roomtype.dto.RoomTypeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    private static final String TYPE_IS_ALREADY_EXISTS = "Such type of room is already exists";
    private static final String TYPE_IS_NOT_EXISTS = "Such type of room is not exists";
    private static final String ROOMS_OF_SUCH_TYPE_ARE_EXISTS =
            "There are rooms of this type at hotel. At first delete rooms";
    private static final String SUCCESSFUL_ACTION_WITH_ROOM_TYPE = "Successful %s room type with id: {}";

    @Override
    public RoomTypeDto save(RoomTypeRequest roomTypeRequest) {
        log.info("Saving room type with type: {}", roomTypeRequest.getType().toLowerCase().strip());

        if (roomTypeRepository.existsByType(roomTypeRequest.getType().toLowerCase().strip())) {
            throw new DuplicateObjectException(TYPE_IS_ALREADY_EXISTS);
        }
        RoomType newRoomType = new RoomType();
        newRoomType.setType(roomTypeRequest.getType().toLowerCase().strip());

        RoomType savedRoomType = roomTypeRepository.save(newRoomType);
        log.info(String.format(SUCCESSFUL_ACTION_WITH_ROOM_TYPE, "created"), savedRoomType.getId());
        return roomTypeMapper.toDto(savedRoomType);
    }

    @Override
    public List<RoomTypeDto> findAll() {
        return roomTypeRepository.findAll().stream()
                .map(roomTypeMapper::toDto)
                .toList();
    }

    @Override
    public RoomTypeDto findByType(String type) {
        return roomTypeMapper.toDto(roomTypeRepository
                .findByType(type.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS)));
    }

    @Override
    public RoomTypeDto findById(Long id) {
        return roomTypeMapper.toDto(roomTypeRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS)));
    }

    @Override
    public void update(Long id, RoomTypeRequest roomTypeRequest) {
        log.info("Updating room type with id: {}", id);
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(TYPE_IS_NOT_EXISTS));

        if (roomTypeRepository.existsByType(roomTypeRequest.getType().toLowerCase().strip())) {
            throw new DuplicateObjectException(TYPE_IS_ALREADY_EXISTS);
        }

        roomType.setType(roomTypeRequest.getType().toLowerCase().strip());
        roomTypeRepository.save(roomType);
        log.info(String.format(SUCCESSFUL_ACTION_WITH_ROOM_TYPE, "updated"), id);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting room type with id: {}", id);

        if (roomTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS))
                .getRooms()
                .isEmpty()) {
            roomTypeRepository.deleteById(id);
            log.info(String.format(SUCCESSFUL_ACTION_WITH_ROOM_TYPE, "deleted"), id);
        } else {
            throw new NotEmptyObjectException(ROOMS_OF_SUCH_TYPE_ARE_EXISTS);
        }
    }
}