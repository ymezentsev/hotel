package com.robot.hotel.reservation;

import com.robot.hotel.guest.Guest;
import com.robot.hotel.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReservationMapper {
    private final ReservationRepository reservationRepository;

    public ReservationDto buildReservationDto(Reservation reservation) {
        //Set<Guest> guestsSet = guestRepository.findByReservationsId(reservation.getId());

        return ReservationDto.builder()
                .id(reservation.getId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .roomNumber(reservation.getRoom().getNumber())
                .guests(reservation.getGuests().stream()
                        .map(getGuestString())
                        .toList())
                .build();
    }

    public Reservation buildReservationFromRequest(ReservationRequest reservationRequest, Room room, List<Guest> guests) {
        return Reservation.builder()
                .room(room)
                .checkInDate(reservationRequest.getCheckInDate())
                .checkOutDate(reservationRequest.getCheckOutDate())
                .guests(guests)
                .build();
    }

    private Function<Guest, String> getGuestString() {
        return guest -> "id:" + guest.getId().toString()
                + ", " + guest.getFirstName() + " " + guest.getLastName()
                + ", " + guest.getTelNumber()
                + ", " + guest.getEmail();
    }
}
