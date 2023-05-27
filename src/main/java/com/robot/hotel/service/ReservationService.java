package com.robot.hotel.service;

import com.robot.hotel.domain.Guests;
import com.robot.hotel.domain.Reservations;
import com.robot.hotel.domain.Rooms;
import com.robot.hotel.dto.ReservationsDto;
import com.robot.hotel.dto.RoomsDto;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.repository.GuestsRepository;
import com.robot.hotel.repository.ReservationsRepository;
import com.robot.hotel.repository.RoomsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationsRepository reservationsRepository;
    private final RoomsRepository roomsRepository;
    private final GuestsRepository guestsRepository;
    private final RoomsService roomsService;

    public List<ReservationsDto> findAll() {
        return reservationsRepository.findAll().stream()
                .map(this::buildReservationsDto)
                .collect(Collectors.toList());
    }

    public Optional<ReservationsDto> findById(Long id) {
        return reservationsRepository.findById(id).map(this::buildReservationsDto);
    }

    public List<ReservationsDto> findReservationsByGuestsId(Long guestId) {
        return reservationsRepository.findReservationsByGuestsId(guestId).stream()
                .map(this::buildReservationsDto)
                .collect(Collectors.toList());
    }

    public List<ReservationsDto> findReservationsByRoom(String roomNumber) {
        if (roomsRepository.findRoomsByNumber(roomNumber).isPresent()) {
            return reservationsRepository
                    .findReservationsByRoomId(roomsRepository.findRoomsByNumber(roomNumber).get()
                            .getId()).stream()
                    .map(this::buildReservationsDto)
                    .collect(Collectors.toList());
        } else {
            throw new NoSuchElementException("Such room is not exists");
        }
    }

    private ReservationsDto buildReservationsDto(Reservations reservation) {
        Set<Guests> guestsSet = guestsRepository.findGuestsByReservationsId(reservation.getId());

        return ReservationsDto.builder()
                .id(reservation.getId())
                .checkInDate(reservation.getCheckInDate())
                .CheckOutDate(reservation.getCheckOutDate())
                .roomNumber(reservation.getRoom().getNumber())
                .guests(guestsSet.stream()
                        .map(getGuestsString())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Function<Guests, String> getGuestsString() {
        return guests -> "Id:" + guests.getId().toString()
                + ", " + guests.getFirstName() + " " + guests.getLastName()
                + ", " + guests.getTelNumber()
                + ", " + guests.getEmail();
    }

    public void save(ReservationsDto reservationsDto)
            throws NoSuchElementException, GuestsQuantityException, WrongDatesException {
        Reservations reservation = buildReservations(reservationsDto);
        reservationsRepository.save(reservation);
    }

    private Reservations buildReservations(ReservationsDto reservationsDto)
            throws NoSuchElementException, GuestsQuantityException, WrongDatesException {
        String roomNumber = reservationsDto.getRoomNumber().toLowerCase();
        Rooms room = null;

        if (roomsRepository.findRoomsByNumber(roomNumber).isPresent()) {
            room = roomsRepository.findRoomsByNumber(roomNumber).get();
        } else {
            throw new NoSuchElementException("Such room is not exists");
        }

        Set<Guests> guestsHashSet = new HashSet<>();
        Set<Long> idGuests = reservationsDto.getGuests().stream().map(Long::parseLong).collect(Collectors.toSet());

        if (idGuests.size() == 0) {
            throw new GuestsQuantityException("You must enter a guest ID");
        }
        if (idGuests.size() > room.getMaxCountOfGuests()) {
            throw new GuestsQuantityException("The number of guests exceeds the maximum allowed in this room");
        }

        for (Long idGuest : idGuests) {
            if (guestsRepository.findById(idGuest).isPresent()) {
                guestsHashSet.add(guestsRepository.findById(idGuest).get());
            } else {
                throw new NoSuchElementException("Such guest is not exists");
            }
        }

        LocalDate now = LocalDate.now();
        if (ChronoUnit.DAYS.between(reservationsDto.getCheckInDate(), now) > 0) {
            throw new WrongDatesException("The check-in date can't be less than current date");
        }
        if (ChronoUnit.DAYS.between(reservationsDto.getCheckInDate(), reservationsDto.getCheckOutDate()) <= 0) {
            throw new WrongDatesException("The check-in date can't be more or equals than check-out date");
        }
        if (ChronoUnit.DAYS.between(reservationsDto.getCheckInDate(), reservationsDto.getCheckOutDate()) > 100) {
            throw new WrongDatesException("You can't reserve room for more than 100 days");
        }
        if (ChronoUnit.DAYS.between(now, reservationsDto.getCheckInDate()) > 180) {
            throw new WrongDatesException("Reservation of rooms opens 180 days in advance");
        }

        Set<RoomsDto> availableRooms = roomsService.findAvailableRooms(reservationsDto.getCheckInDate().toString(), reservationsDto.getCheckOutDate().toString());
        RoomsDto roomsDto = roomsService.buildRoomsDto(room);
        if(!availableRooms.contains(roomsDto)){
            throw new WrongDatesException("This room is occupied for your dates");
        }

        return Reservations.builder()
                .checkInDate(reservationsDto.getCheckInDate())
                .CheckOutDate(reservationsDto.getCheckOutDate())
                .room(room)
                .guests(guestsHashSet)
                .build();
    }

    public List<ReservationsDto> findCurrentReservations() {
        LocalDate now = LocalDate.now();

        return reservationsRepository.findCurrentReservations(now)
                .stream().map(this::buildReservationsDto)
                .toList();
    }

    public void deleteById(Long id) throws NoSuchElementException {
        if(findById(id).isEmpty()){
            throw new NoSuchElementException("Such reservation is not exists");
        }

        reservationsRepository.deleteById(id);
    }
}
