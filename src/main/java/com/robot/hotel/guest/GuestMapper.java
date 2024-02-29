package com.robot.hotel.guest;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GuestMapper {
    private final ReservationRepository reservationRepository;

    public GuestDto buildGuestsDto(Guest guest) {
        //List<Reservation> reservations = reservationRepository.findByGuestsId(guest.getId());

        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .telNumber(guest.getTelNumber())
                .email(guest.getEmail())
                .passportSerialNumber(guest.getPassportSerialNumber())
                .reservations(guest.getReservations().stream()
                        .map(getReservationsString())
                        .toList())
                .build();
    }

    public Guest buildGuestFromRequest(GuestRequest guestRequest) {
        if (Objects.isNull(guestRequest.getPassportSerialNumber())) {
            guestRequest.setPassportSerialNumber("");
        }

        return Guest.builder()
                .firstName(guestRequest.getFirstName().toLowerCase().strip())
                .lastName(guestRequest.getLastName().toLowerCase().strip())
                .telNumber(guestRequest.getTelNumber().strip())
                .email(guestRequest.getEmail().toLowerCase().strip())
                .passportSerialNumber(guestRequest.getPassportSerialNumber().toLowerCase().strip())
                .reservations(Collections.emptyList())
                .build();
    }

    private Function<Reservation, String> getReservationsString() {
        return reservation -> "Id:" + reservation.getId().toString()
                + ", " + reservation.getCheckInDate().toString() + " - "
                + reservation.getCheckOutDate().toString();
    }
}
