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

    public List<RoomDto> findAll() {
        return roomRepository.findAll().stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
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

    public Room save(RoomDto roomsDto) {
        if (findRoomsByNumber(roomsDto.getNumber()).isPresent()) {
            throw new DuplicateObjectException("Such room is already exists");
        } else {
            Room room = buildRoom(roomsDto);
            return roomRepository.save(room);
        }
    }

    private Room buildRoom(RoomDto roomsDto) {
        String type = roomsDto.getRoomType().toLowerCase();
        RoomType roomType = null;

        if (roomTypeRepository.findRoomTypeByType(type).isPresent()) {
            roomType = roomTypeRepository.findRoomTypeByType(type).get();
        } else {
            throw new NoSuchElementException("Such type of room is not exists");
        }
        return Room.builder()
                .number(roomsDto.getNumber().toLowerCase())
                .price(roomsDto.getPrice())
                .maxCountOfGuests(roomsDto.getMaxCountOfGuests())
                .roomType(roomType)
                .build();
    }

    public Optional<RoomDto> findById(Long id) {
        return roomRepository.findById(id).map(this::buildRoomsDto);
    }

    public Optional<RoomDto> findRoomsByNumber(String number) {
        return roomRepository.findRoomsByNumber(number.toLowerCase()).map(this::buildRoomsDto);
    }

    public List<RoomDto> findByType(String type) {
        Long id = roomTypeRepository.findRoomTypeByType(type).orElseThrow().getId();
        if (id == null) {
            throw new NoSuchElementException("Room type is not exists");
        }

        return roomRepository.findRoomsByRoomTypeId(id).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findByPriceMoreThanOrEqual(BigDecimal sum) {
        return roomRepository.findRoomsByPriceMoreThanOrEqual(sum).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findByPriceLessThanOrEqual(BigDecimal sum) {
        return roomRepository.findRoomsByPriceLessThanOrEqual(sum).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findByGuestsCount(int guestCount) {
        return roomRepository.findRoomsByGuestsCount(guestCount).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public Set<RoomDto> findAvailableRooms(String checkInString, String checkOutString) {
        LocalDate checkIn = LocalDate.parse(checkInString);
        LocalDate checkOut = LocalDate.parse(checkOutString);
        if (ChronoUnit.DAYS.between(checkIn, checkOut) <= 0) {
            throw new WrongDatesException("The check-in date can't be more or equals than check-out date");
        }

        List<RoomDto> roomsDtoWithMatchDate = reservationRepository.findAvailableRooms(checkIn, checkOut)
                .stream().map(id -> findById(id).get())
                .toList();

        List<Room> roomsWithoutReservations = roomRepository.findAll()
                .stream().filter(room -> room.getReservations().size() == 0)
                .toList();
        List<RoomDto> roomsDtoWithoutReservations = roomsWithoutReservations.stream()
                .map(this::buildRoomsDto).toList();

        Set<RoomDto> availableRoomsDto = new HashSet<>(roomsDtoWithMatchDate);
        availableRoomsDto.addAll(roomsDtoWithoutReservations);

        return availableRoomsDto;
    }

    public void update(String number, RoomDto roomsDto) {
        Optional<RoomDto> optionalRoom = findRoomsByNumber(number.toLowerCase());
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
        roomRepository.save(room);
    }

    public void deleteById(Long id) {
        if (roomRepository.findById(id).orElseThrow().getReservations().isEmpty()) {
            roomRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException("There are reservations for this room. At first delete reservations.");
        }
    }
}
