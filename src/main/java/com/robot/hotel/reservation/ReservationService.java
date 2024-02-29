package com.robot.hotel.reservation;

import com.robot.hotel.exception.GuestsQuantityException;
import com.robot.hotel.exception.WrongDatesException;
import com.robot.hotel.guest.Guest;
import com.robot.hotel.guest.GuestRepository;
import com.robot.hotel.room.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final RoomMapper roomMapper;

    private static final String RESERVATION_IS_NOT_EXISTS = "Such reservation is not exists";
    private static final String ROOM_IS_NOT_EXISTS = "Such room is not exists";
    private static final String GUEST_IS_NOT_EXISTS = "Such guest is not exists";
    private static final String CHECK_OUT_LESS_THAN_CHECK_IN_DATE = "The check out date must be after check in date";
    private static final String TOO_LONG_RESERVATION = "You can't reserve room for more than 60 days";
    private static final String TOO_EARLY_RESERVATION = "Reservation of rooms opens 180 days in advance";
    private static final String TOO_MANY_GUESTS = "The quantity of guests exceeds the maximum allowed in this room";
    private static final String OCCUPIED_ROOM = "This room is occupied for your dates";

    public List<ReservationDto> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::buildReservationDto)
                .toList();
    }

    public ReservationDto findById(Long id) {
        return reservationMapper.buildReservationDto(reservationRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(RESERVATION_IS_NOT_EXISTS)));
    }

    public List<ReservationDto> findReservationsByGuestsId(Long guestId) {
        return reservationRepository.findByGuestsId(guestId).stream()
                .map(reservationMapper::buildReservationDto)
                .toList();
    }

    public List<ReservationDto> findReservationsByRoom(String roomNumber) {
        Long roomId = roomService.findByNumber(roomNumber).getId();

        return reservationRepository.findByRoomId(roomId).stream()
                .map(reservationMapper::buildReservationDto)
                .toList();
    }

    public ReservationDto save(ReservationRequest reservationRequest) {
        Room room = roomRepository.findByNumber(reservationRequest.getRoomNumber().toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(ROOM_IS_NOT_EXISTS));

        if (reservationRequest.getCheckOutDate().isBefore(reservationRequest.getCheckInDate())
                || reservationRequest.getCheckOutDate().isEqual(reservationRequest.getCheckInDate())) {
            throw new WrongDatesException(CHECK_OUT_LESS_THAN_CHECK_IN_DATE);
        }

        if (ChronoUnit.DAYS.between(reservationRequest.getCheckInDate(), reservationRequest.getCheckOutDate()) > 60) {
            throw new WrongDatesException(TOO_LONG_RESERVATION);
        }

        if (ChronoUnit.DAYS.between(LocalDate.now(), reservationRequest.getCheckOutDate()) > 180) {
            throw new WrongDatesException(TOO_EARLY_RESERVATION);
        }

        if (reservationRequest.getGuests().size() > room.getMaxCountOfGuests()) {
            throw new GuestsQuantityException(TOO_MANY_GUESTS);
        }

        List<Guest> guests = reservationRequest.getGuests().stream()
                .map(guestId -> guestRepository.findById(Long.parseLong(guestId))
                        .orElseThrow(() -> new NoSuchElementException(GUEST_IS_NOT_EXISTS)))
                .toList();

        Set<RoomDto> freeRooms = roomService.findFreeRooms(new FreeRoomRequest(
                reservationRequest.getCheckInDate(), reservationRequest.getCheckOutDate()));
        if (!freeRooms.contains(roomMapper.buildRoomDto(room))) {
            throw new WrongDatesException(OCCUPIED_ROOM);
        }

        Reservation newReservation = reservationMapper.buildReservationFromRequest(reservationRequest, room, guests);
        return reservationMapper.buildReservationDto(reservationRepository.save(newReservation));
    }

    public List<ReservationDto> findCurrentReservations() {
        return reservationRepository.findCurrentReservations()
                .stream()
                .map(reservationMapper::buildReservationDto)
                .toList();
    }

    public List<ReservationDto> findCurrentReservationsForSpecificRoom(String roomNumber) {
        Long roomId = roomService.findByNumber(roomNumber).getId();

        return reservationRepository.findCurrentReservationsForRoom(roomId)
                .stream()
                .map(reservationMapper::buildReservationDto)
                .toList();
    }

    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NoSuchElementException(RESERVATION_IS_NOT_EXISTS);
        }
        reservationRepository.deleteById(id);
    }
}
