package com.robot.hotel.guest;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestMapper {
    private final ReservationRepository reservationRepository;

    public GuestDto buildGuestsDto(Guest guest) {
        List<Reservation> reservations = reservationRepository.findByGuestsId(guest.getId());

        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .telNumber(guest.getTelNumber())
                .email(guest.getEmail())
                .passportSerialNumber(guest.getPassportSerialNumber())
                .reservations(reservations.stream()
                        .map(getReservationsString())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Function<Reservation, String> getReservationsString() {
        return reservation -> "Id:" + reservation.getId().toString()
                + ", " + reservation.getCheckInDate().toString() + " - "
                + reservation.getCheckOutDate().toString();
    }
}
