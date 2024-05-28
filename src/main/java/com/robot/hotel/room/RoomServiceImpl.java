package com.robot.hotel.room;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.room.dto.FreeRoomRequest;
import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.room.dto.RoomRequest;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.roomtype.RoomTypeRepository;
import com.robot.hotel.roomtype.RoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomTypeService roomTypeService;
    private final RoomTypeRepository roomTypeRepository;
    private final ReservationRepository reservationRepository;
    private final RoomMapper roomMapper;

    private static final String NUMBER_IS_ALREADY_EXISTS = "Such number is already exists";
    private static final String CHECK_OUT_LESS_THAN_CHECK_IN_DATE = "The check out date must be after check in date";
    private static final String ROOM_IS_NOT_EXISTS = "Such room is not exists";
    private static final String TYPE_IS_NOT_EXISTS = "Such type of room is not exists";
    private static final String RESERVATIONS_FOR_THIS_ROOM_ARE_EXISTS =
            "There are reservations for this room. At first delete reservations";
    private static final String SUCCESSFUL_ACTION_WITH_ROOM = "Successful %s room with id: {}";

    @Override
    public Page<RoomDto> findAll(Pageable pageable) {
        return roomRepository.findAll(pageable)
                .map(roomMapper::toDto);
    }

    @Override
    public RoomDto save(RoomRequest roomRequest) {
        log.info("Saving room with number: {}", roomRequest.getNumber().toLowerCase().strip());

        if (roomRepository.existsByNumber(roomRequest.getNumber().toLowerCase().strip())) {
            throw new DuplicateObjectException(NUMBER_IS_ALREADY_EXISTS);
        }

        RoomType roomType = roomTypeRepository.findByType(roomRequest.getRoomType().toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS));

        Room newRoom = Room.builder()
                .number(roomRequest.getNumber().toLowerCase().strip())
                .price(roomRequest.getPrice())
                .maxCountOfGuests(roomRequest.getMaxCountOfGuests())
                .roomType(roomType)
                .build();

        Room savedRoom = roomRepository.save(newRoom);
        log.info(String.format(SUCCESSFUL_ACTION_WITH_ROOM, "created"), savedRoom.getId());
        return roomMapper.toDto(savedRoom);
    }

    @Override
    public RoomDto findById(Long id) {
        return roomMapper.toDto(roomRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(ROOM_IS_NOT_EXISTS)));
    }

    @Override
    public RoomDto findByNumber(String number) {
        return roomMapper.toDto(roomRepository
                .findByNumber(number.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(ROOM_IS_NOT_EXISTS)));
    }

    @Override
    public Page<RoomDto> findByType(String type, Pageable pageable) {
        Long roomTypeId = roomTypeService.findByType(type).id();

        return roomRepository.findByRoomTypeId(roomTypeId, pageable)
                .map(roomMapper::toDto);
    }

    @Override
    public Page<RoomDto> findByPriceMoreThanOrEqual(BigDecimal price, Pageable pageable) {
        return roomRepository.findByPriceMoreThanOrEqual(price, pageable)
                .map(roomMapper::toDto);
    }

    @Override
    public Page<RoomDto> findByPriceLessThanOrEqual(BigDecimal price, Pageable pageable) {
        return roomRepository.findByPriceLessThanOrEqual(price, pageable)
                .map(roomMapper::toDto);
    }

    @Override
    public Page<RoomDto> findByGuestsCount(int guestCount, Pageable pageable) {
        return roomRepository.findByGuestsCount(guestCount, pageable)
                .map(roomMapper::toDto);
    }

    @Override
    public Set<RoomDto> findFreeRooms(FreeRoomRequest freeRoomRequest) {
        if (freeRoomRequest.getCheckOutDate().isBefore(freeRoomRequest.getCheckInDate())
                || freeRoomRequest.getCheckOutDate().isEqual(freeRoomRequest.getCheckInDate())) {
            throw new WrongDatesException(CHECK_OUT_LESS_THAN_CHECK_IN_DATE);
        }

        List<RoomDto> roomsDtoWithMatchDates = reservationRepository.findFreeRoomsWithReservations(
                        freeRoomRequest.getCheckInDate(), freeRoomRequest.getCheckOutDate())
                .stream()
                .map(roomMapper::toDto)
                .toList();

        List<RoomDto> roomsDtoWithoutReservations = roomRepository.findRoomsWithoutReservations().stream()
                .map(roomMapper::toDto)
                .toList();

        Set<RoomDto> freeRoomsDto = new HashSet<>(roomsDtoWithMatchDates);
        freeRoomsDto.addAll(roomsDtoWithoutReservations);
        return freeRoomsDto;
    }

    @Override
    public void update(Long id, RoomRequest roomRequest) {
        log.info("Updating room with id: {}", id);
        Room roomToUpdate = roomRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(ROOM_IS_NOT_EXISTS)
        );

        Optional<Room> existingRoom = roomRepository.findByNumber(roomRequest.getNumber().toLowerCase().strip());
        if (existingRoom.isPresent() && !existingRoom.get().getId().equals(id)) {
            throw new DuplicateObjectException(NUMBER_IS_ALREADY_EXISTS);
        }

        RoomType roomType = roomTypeRepository.findByType(roomRequest.getRoomType().toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS));

        roomToUpdate.setNumber(roomRequest.getNumber().toLowerCase().strip());
        roomToUpdate.setPrice(roomRequest.getPrice());
        roomToUpdate.setMaxCountOfGuests(roomRequest.getMaxCountOfGuests());
        roomToUpdate.setRoomType(roomType);
        roomRepository.save(roomToUpdate);

        log.info(String.format(SUCCESSFUL_ACTION_WITH_ROOM, "updated"), id);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting room with id: {}", id);

        if (roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ROOM_IS_NOT_EXISTS))
                .getReservations()
                .isEmpty()) {
            roomRepository.deleteById(id);
            log.info(String.format(SUCCESSFUL_ACTION_WITH_ROOM, "deleted"), id);
        } else {
            throw new NotEmptyObjectException(RESERVATIONS_FOR_THIS_ROOM_ARE_EXISTS);
        }
    }
}