package com.robot.hotel.room;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.roomtype.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ReservationRepository reservationRepository;
    private final RoomMapper roomMapper;

    private static final String NUMBER_IS_ALREADY_EXISTS = "Such number is already exists";
    private static final String TYPE_IS_NOT_EXISTS = "Such type of room is not exists";

    public List<RoomDto> findAll() {
        return roomRepository.findAll().stream()
                .map(roomMapper::buildRoomsDto)
                .toList();
    }

    public RoomDto buildRoomsDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .number(room.getNumber())
                .price(room.getPrice())
                .maxCountOfGuests(room.getMaxCountOfGuests())
                .roomType(room.getRoomType().getType())
                .reservations(room.getReservations().stream()
                        .map(getReservationsString())
                        .collect(Collectors.toList()))
                .build();
    }

    private static Function<Reservation, String> getReservationsString() {
        return reservations -> "Id:" + reservations.getId().toString()
                + ", " + reservations.getCheckInDate().toString() + " - "
                + reservations.getCheckOutDate().toString();
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

    public Set<RoomDto> findAvailableRooms(String checkInString, String checkOutString) {
        LocalDate checkIn = LocalDate.parse(checkInString);
        LocalDate checkOut = LocalDate.parse(checkOutString);
        if (ChronoUnit.DAYS.between(checkIn, checkOut) <= 0) {
            throw new WrongDatesException("The check-in date can't be more or equals than check-out date");
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

    public void update(String number, RoomDto roomsDto) {
/*        Optional<RoomDto> optionalRoom = findByNumber(number.toLowerCase());
        Long id;
        RoomDto roomDto = null;

        if (optionalRoom.isEmpty()) {
            throw new NoSuchElementException("Room number " + number + " is not exists");
        } else {
            roomDto = optionalRoom.get();
            id = roomDto.getId();
        }

        if (roomsDto.getPrice() != null) {
            roomDto.setPrice(roomsDto.getPrice());
        }
        if (roomsDto.getMaxCountOfGuests() != 0) {
            roomDto.setMaxCountOfGuests(roomsDto.getMaxCountOfGuests());
        }
        if (!roomsDto.getRoomType().isEmpty()) {
            roomDto.setRoomType(roomsDto.getRoomType());
        }

        Room room = buildRoom(roomDto);
        room.setId(id);
        roomRepository.save(room);*/
    }

    public void deleteById(Long id) {
        if (roomRepository.findById(id).orElseThrow().getReservations().isEmpty()) {
            roomRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException("There are reservations for this room. At first delete reservations.");
        }
    }
}
