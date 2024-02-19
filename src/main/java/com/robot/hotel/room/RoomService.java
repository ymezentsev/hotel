package com.robot.hotel.room;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.roomtype.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ReservationRepository reservationRepository;
    private final RoomMapper roomMapper;

    private static final String NUMBER_IS_ALREADY_EXISTS = "Such number is already exists";
    private static final String TYPE_IS_NOT_EXISTS = "Such type of room is not exists";
    private static final String ROOM_IS_NOT_EXISTS = "Room with id %d is not exists";
    private static final String RESERVATIONS_FOR_THIS_ROOM_ARE_EXISTS =
            "There are reservations for this room. At first delete reservations";

    public List<RoomDto> findAll() {
        return roomRepository.findAll().stream()
                .map(roomMapper::buildRoomsDto)
                .toList();
    }

    public RoomDto save(RoomRequest roomRequest) {
        if (Boolean.TRUE.equals(roomRepository.existsByNumber(roomRequest.getNumber().toLowerCase()))) {
            throw new DuplicateObjectException(NUMBER_IS_ALREADY_EXISTS);
        }

        RoomType roomType = roomTypeRepository.findByType(roomRequest.getRoomType().toLowerCase())
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS));

        Room newRoom = roomMapper.buildRoomFromRequest(roomRequest, roomType);
        return roomMapper.buildRoomsDto(roomRepository.save(newRoom));
    }

    public RoomDto findById(Long id) {
        return roomMapper.buildRoomsDto(roomRepository
                .findById(id)
                .orElseThrow());
    }

    public RoomDto findByNumber(String number) {
        return roomMapper.buildRoomsDto(roomRepository
                .findByNumber(number.toLowerCase())
                .orElseThrow());
    }

    public List<RoomDto> findByType(String type) {
        Long roomTypeId = roomTypeRepository.findByType(type.toLowerCase())
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS))
                .getId();

        return roomRepository.findByRoomTypeId(roomTypeId).stream()
                .map(roomMapper::buildRoomsDto)
                .toList();
    }

    public List<RoomDto> findByPriceMoreThanOrEqual(BigDecimal price) {
        return roomRepository.findByPriceMoreThanOrEqual(price).stream()
                .map(roomMapper::buildRoomsDto)
                .toList();
    }

    public List<RoomDto> findByPriceLessThanOrEqual(BigDecimal price) {
        return roomRepository.findByPriceLessThanOrEqual(price).stream()
                .map(roomMapper::buildRoomsDto)
                .toList();
    }

    public List<RoomDto> findByGuestsCount(int guestCount) {
        return roomRepository.findByGuestsCount(guestCount).stream()
                .map(roomMapper::buildRoomsDto)
                .toList();
    }

    public Set<RoomDto> findFreeRooms(FreeRoomRequest freeRoomRequest) {
        if (freeRoomRequest.checkOutDate.isBefore(freeRoomRequest.checkInDate)
                || freeRoomRequest.checkOutDate.isEqual(freeRoomRequest.checkInDate)) {
            throw new WrongDatesException("The check out date must be after check in date");
        }

/*        List<RoomDto> roomsDtoWithMatchDate = reservationRepository.findAvailableRooms(checkIn, checkOut)
                .stream().map(id -> findById(id).get())
                .toList();

        List<Room> roomsWithoutReservations = roomRepository.findAll()
                .stream().filter(room -> room.getReservations().size() == 0)
                .toList();
        List<RoomDto> roomsDtoWithoutReservations = roomsWithoutReservations.stream()
                .map(this::buildRoomsDto).toList();

        Set<RoomDto> availableRoomsDto = new HashSet<>(roomsDtoWithMatchDate);
        availableRoomsDto.addAll(roomsDtoWithoutReservations);

        return availableRoomsDto;*/
        return new HashSet<>();
    }

    public void update(Long id, RoomRequest roomRequest) {
        Room roomToUpdate = roomRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(String.format(ROOM_IS_NOT_EXISTS, id))
        );

        if (Boolean.TRUE.equals(roomRepository.existsByNumber(roomRequest.getNumber().toLowerCase()))) {
            Long idExistingRoom = roomRepository.findByNumber(roomRequest.getNumber().toLowerCase())
                    .orElseThrow()
                    .getId();
            if (!Objects.equals(idExistingRoom, id)) {
                throw new DuplicateObjectException(NUMBER_IS_ALREADY_EXISTS);
            }
        }

        RoomType roomType = roomTypeRepository.findByType(roomRequest.getRoomType().toLowerCase())
                .orElseThrow(() -> new NoSuchElementException(TYPE_IS_NOT_EXISTS));

        roomToUpdate.setNumber(roomRequest.getNumber().toLowerCase());
        roomToUpdate.setPrice(roomRequest.getPrice());
        roomToUpdate.setMaxCountOfGuests(roomRequest.getMaxCountOfGuests());
        roomToUpdate.setRoomType(roomType);
        roomRepository.save(roomToUpdate);
    }

    public void deleteById(Long id) {
        if (roomRepository.findById(id).orElseThrow().getReservations().isEmpty()) {
            roomRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException(RESERVATIONS_FOR_THIS_ROOM_ARE_EXISTS);
        }
    }
}
