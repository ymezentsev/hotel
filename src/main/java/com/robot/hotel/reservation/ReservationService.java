package com.robot.hotel.reservation;

import com.robot.hotel.guest.Guest;
import com.robot.hotel.room.*;
import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.guest.GuestRepository;
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
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public List<ReservationDto> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::buildReservationsDto)
                .collect(Collectors.toList());
    }

    public Optional<ReservationDto> findById(Long id) {
        return reservationRepository.findById(id).map(this::buildReservationsDto);
    }

    public List<ReservationDto> findReservationsByGuestsId(Long guestId) {
        return reservationRepository.findReservationsByGuestsId(guestId).stream()
                .map(this::buildReservationsDto)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> findReservationsByRoom(String roomNumber) {
        if (roomRepository.findByNumber(roomNumber).isPresent()) {
            return reservationRepository
                    .findReservationsByRoomId(roomRepository.findByNumber(roomNumber).get()
                            .getId()).stream()
                    .map(this::buildReservationsDto)
                    .collect(Collectors.toList());
        } else {
            throw new NoSuchElementException("Such room is not exists");
        }
    }

    private ReservationDto buildReservationsDto(Reservation reservation) {
        Set<Guest> guestsSet = guestRepository.findGuestsByReservationsId(reservation.getId());

        return ReservationDto.builder()
                .id(reservation.getId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .roomNumber(reservation.getRoom().getNumber())
                .guests(guestsSet.stream()
                        .map(getGuestsString())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Function<Guest, String> getGuestsString() {
        return guests -> "Id:" + guests.getId().toString()
                + ", " + guests.getFirstName() + " " + guests.getLastName()
                + ", " + guests.getTelNumber()
                + ", " + guests.getEmail();
    }

    public Reservation save(ReservationDto reservationsDto){
        Reservation reservation = buildReservations(reservationsDto);
        return reservationRepository.save(reservation);
    }

    private Reservation buildReservations(ReservationDto reservationsDto){
        /*String roomNumber = reservationsDto.getRoomNumber().toLowerCase();
        Room room = null;

        if (roomRepository.findByNumber(roomNumber).isPresent()) {
            room = roomRepository.findByNumber(roomNumber).get();
        } else {
            throw new NoSuchElementException("Such room is not exists");
        }

        Set<Guest> guestsHashSet = new HashSet<>();
        Set<Long> idGuests = reservationsDto.getGuests().stream().map(Long::parseLong).collect(Collectors.toSet());

        if (idGuests.size() == 0) {
            throw new GuestsQuantityException("You must enter a guest ID");
        }
        if (idGuests.size() > room.getMaxCountOfGuests()) {
            throw new GuestsQuantityException("The number of guests exceeds the maximum allowed in this room");
        }

        for (Long idGuest : idGuests) {
            if (guestRepository.findById(idGuest).isPresent()) {
                guestsHashSet.add(guestRepository.findById(idGuest).get());
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

        Set<RoomDto> availableRooms = roomService.findAvailableRooms(reservationsDto.getCheckInDate().toString(), reservationsDto.getCheckOutDate().toString());
        RoomDto roomsDto = roomMapper.buildRoomsDto(room);
        if(!availableRooms.contains(roomsDto)){
            throw new WrongDatesException("This room is occupied for your dates");
        }

        return Reservation.builder()
                .checkInDate(reservationsDto.getCheckInDate())
                .checkOutDate(reservationsDto.getCheckOutDate())
                .room(room)
                .guests(guestsHashSet)
                .build();*/
        return new Reservation();
    }

    public List<ReservationDto> findCurrentReservations() {
        LocalDate now = LocalDate.now();

        return reservationRepository.findCurrentReservations(now)
                .stream().map(this::buildReservationsDto)
                .toList();
    }

    public void deleteById(Long id) {
        if(findById(id).isEmpty()){
            throw new NoSuchElementException("Such reservation is not exists");
        }

        reservationRepository.deleteById(id);
    }
}
