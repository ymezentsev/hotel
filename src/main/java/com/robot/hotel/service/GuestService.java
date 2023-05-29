package com.robot.hotel.service;

import com.robot.hotel.domain.Guest;
import com.robot.hotel.domain.Reservation;
import com.robot.hotel.dto.GuestDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.repository.GuestRepository;
import com.robot.hotel.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;


    public List<GuestDto> findAll() {
        return guestRepository.findAll().stream()
                .map(this::buildGuestsDto)
                .collect(Collectors.toList());
    }

    public List<GuestDto> findGuestByReservation(Long id) {
        return guestRepository.findGuestsByReservationsId(id).stream()
                .map(this::buildGuestsDto)
                .collect(Collectors.toList());
    }

    private GuestDto buildGuestsDto(Guest guest) {
        List<Reservation> reservationsByGuestsId = reservationRepository.findReservationsByGuestsId(guest.getId());

        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .telNumber(guest.getTelNumber())
                .email(guest.getEmail())
                .passportSerialNumber(guest.getPassportSerialNumber())
                .reservations(reservationsByGuestsId.stream()
                        .map(getReservationsString())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Function<Reservation, String> getReservationsString() {
        return reservations -> "Id:" + reservations.getId().toString()
                + ", room:" + reservations.getRoom().getNumber()
                + ", " + reservations.getCheckInDate().toString() + " - "
                + reservations.getCheckOutDate().toString();
    }

    public Guest save(GuestDto guestsDto) throws DublicateObjectException {
        if (findByEmail(guestsDto.getEmail()).isPresent()) {
            throw new DublicateObjectException("Guest with such email is already exists");
        }
        if (findByTelNumber(guestsDto.getTelNumber()).isPresent()) {
            throw new DublicateObjectException("Guest with such tel. number is already exists");
        }
        if (findByPassportSerialNumber(guestsDto.getPassportSerialNumber()).isPresent()) {
            throw new DublicateObjectException("Guest with such passport is already exists");
        }

        Guest guest = buildGuest(guestsDto);
        return guestRepository.save(guest);
    }

    private Guest buildGuest(GuestDto guestsDto) {
        return Guest.builder()
                .firstName(guestsDto.getFirstName().toLowerCase())
                .lastName(guestsDto.getLastName().toLowerCase())
                .telNumber(guestsDto.getTelNumber())
                .email(guestsDto.getEmail().toLowerCase())
                .passportSerialNumber(guestsDto.getPassportSerialNumber().toLowerCase())
                .build();
    }

    public Optional<GuestDto> findById(Long id) {
        return guestRepository.findById(id).map(this::buildGuestsDto);
    }

    public Optional<GuestDto> findByEmail(String email) {
        return guestRepository.findGuestsByEmail(email.toLowerCase()).map(this::buildGuestsDto);
    }

    public Optional<GuestDto> findByTelNumber(String telNumber) {
        return guestRepository.findGuestsByTelNumber(telNumber.toLowerCase()).map(this::buildGuestsDto);
    }

    public Optional<GuestDto> findByPassportSerialNumber(String passportSerialNumber) {
        return guestRepository.findGuestsByPassportSerialNumber(passportSerialNumber.toLowerCase()).map(this::buildGuestsDto);
    }

    public List<GuestDto> findByLastName(String lastName) {
        return guestRepository.findGuestsByLastName(lastName).stream()
                .map(this::buildGuestsDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, GuestDto newGuestsDto) throws NoSuchElementException, DublicateObjectException {
        Optional<GuestDto> optionalGuestsDto = findById(id);
        GuestDto guestsDto = null;

        if (optionalGuestsDto.isEmpty()) {
            throw new NoSuchElementException("Such guest is not exists");
        } else {
            guestsDto = optionalGuestsDto.get();
        }

        if (newGuestsDto.getFirstName() != null) {
            guestsDto.setFirstName(newGuestsDto.getFirstName());
        }
        if (newGuestsDto.getLastName() != null) {
            guestsDto.setLastName(newGuestsDto.getLastName());
        }
        if (newGuestsDto.getTelNumber() != null) {
            if (findByTelNumber(newGuestsDto.getTelNumber()).isPresent()) {
                throw new DublicateObjectException("Guest with such tel. number is already exists");
            }
            guestsDto.setTelNumber(newGuestsDto.getTelNumber());
        }
        if (newGuestsDto.getEmail() != null) {
            if (findByEmail(newGuestsDto.getEmail()).isPresent()) {
                throw new DublicateObjectException("Guest with such email is already exists");
            }
            guestsDto.setEmail(newGuestsDto.getEmail());
        }
        if (newGuestsDto.getPassportSerialNumber() != null) {
            if (findByPassportSerialNumber(newGuestsDto.getPassportSerialNumber()).isPresent()) {
                throw new DublicateObjectException("Guest with such passport is already exists");
            }
            guestsDto.setPassportSerialNumber(newGuestsDto.getPassportSerialNumber());
        }

        Guest guest = buildGuest(guestsDto);
        guest.setId(id);
        guestRepository.save(guest);
    }

    public void deleteById(Long id) throws NoSuchElementException, NotEmptyObjectException {
        if(findById(id).isEmpty()){
            throw new NoSuchElementException("Such guest is not exists");
        }

        if (reservationRepository.findReservationsByGuestsId(id).isEmpty()) {
            guestRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException("This guest has reservations. At first delete reservations.");
        }
    }
}
