package com.robot.hotel.service;


import com.robot.hotel.domain.Reservations;
import com.robot.hotel.domain.RoomType;
import com.robot.hotel.domain.Rooms;
import com.robot.hotel.dto.RoomsDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.repository.ReservationsRepository;
import com.robot.hotel.repository.RoomTypeRepository;
import com.robot.hotel.repository.RoomsRepository;
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
public class RoomsService {
    private final RoomsRepository roomsRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final ReservationsRepository reservationsRepository;

    public List<RoomsDto> findAll() {
        return roomsRepository.findAll().stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    RoomsDto buildRoomsDto(Rooms room) {
        return RoomsDto.builder()
                .id(room.getId())
                .number(room.getNumber())
                .price(room.getPrice())
                .maxCountOfGuests(room.getMaxCountOfGuests())
                .roomType(room.getRoomType().getType())
                .reservation(room.getReservations().stream()
                        .map(getReservationsString())
                        .collect(Collectors.toList()))
                .build();
    }

    private static Function<Reservations, String> getReservationsString() {
        return reservations -> "Id:" + reservations.getId().toString()
                + ", " + reservations.getCheckInDate().toString() + " - "
                + reservations.getCheckOutDate().toString();
    }

    public void save(RoomsDto roomsDto) throws DublicateObjectException, NoSuchElementException {
        if (findRoomsByNumber(roomsDto.getNumber()).isPresent()) {
            throw new DublicateObjectException("Such room is already exists");
        } else {
            Rooms room = buildRoom(roomsDto);
            roomsRepository.save(room);
        }
    }

    private Rooms buildRoom(RoomsDto roomsDto) throws NoSuchElementException {
        String type = roomsDto.getRoomType().toLowerCase();
        RoomType roomType = null;

        if (roomTypeRepository.findRoomTypeByType(type).isPresent()) {
            roomType = roomTypeRepository.findRoomTypeByType(type).get();
        } else {
            throw new NoSuchElementException("Such type of room is not exists");
        }
        return Rooms.builder()
                .number(roomsDto.getNumber().toLowerCase())
                .price(roomsDto.getPrice())
                .maxCountOfGuests(roomsDto.getMaxCountOfGuests())
                .roomType(roomType)
                .build();
    }

    public Optional<RoomsDto> findById(Long id) {
        return roomsRepository.findById(id).map(this::buildRoomsDto);
    }

    public Optional<RoomsDto> findRoomsByNumber(String number) {
        return roomsRepository.findRoomsByNumber(number.toLowerCase()).map(this::buildRoomsDto);
    }

    public List<RoomsDto> findByType(String type) throws NoSuchElementException {
        Long id = roomTypeRepository.findRoomTypeByType(type).orElseThrow().getId();
        if (id == null) {
            throw new NoSuchElementException("Room type is not exists");
        }

        return roomsRepository.findRoomsByRoomTypeId(id).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public List<RoomsDto> findByPriceMoreThan(BigDecimal sum) {
        return roomsRepository.findRoomsByPriceMoreThan(sum).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public List<RoomsDto> findByPriceLessThan(BigDecimal sum) {
        return roomsRepository.findRoomsByPriceLessThan(sum).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public List<RoomsDto> findByGuestsCount(int guestCount) {
        return roomsRepository.findRoomsByGuestsCount(guestCount).stream()
                .map(this::buildRoomsDto)
                .collect(Collectors.toList());
    }

    public Set<RoomsDto> findAvailableRooms(String checkInString, String checkOutString) throws WrongDatesException {
        LocalDate checkIn = LocalDate.parse(checkInString);
        LocalDate checkOut = LocalDate.parse(checkOutString);
        if (ChronoUnit.DAYS.between(checkIn, checkOut) <= 0) {
            throw new WrongDatesException("The check-in date can't be more or equals than check-out date");
        }

        List<RoomsDto> roomsDtoWithMatchDate = reservationsRepository.findAvailableRooms(checkIn, checkOut)
                .stream().map(id -> findById(id).get())
                .toList();

        List<Rooms> roomsWithoutReservations = roomsRepository.findAll()
                .stream().filter(room -> room.getReservations().size() == 0)
                .toList();
        List<RoomsDto> roomsDtoWithoutReservations = roomsWithoutReservations.stream()
                .map(this::buildRoomsDto).toList();

        Set<RoomsDto> availableRoomsDto = new HashSet<>(roomsDtoWithMatchDate);
        availableRoomsDto.addAll(roomsDtoWithoutReservations);

        return availableRoomsDto;
    }

    public void update(String number, RoomsDto roomsDto) throws NoSuchElementException {
        Optional<RoomsDto> optionalRoom = findRoomsByNumber(number.toLowerCase());
        Long id;
        RoomsDto roomDto = null;

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

        Rooms room = buildRoom(roomDto);
        room.setId(id);
        roomsRepository.save(room);
    }

    public void deleteById(Long id) throws NoSuchElementException, NotEmptyObjectException {
        if (roomsRepository.findById(id).orElseThrow().getReservations().isEmpty()) {
            roomsRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException("There are reservations for this room. At first delete reservations.");
        }
    }
}
