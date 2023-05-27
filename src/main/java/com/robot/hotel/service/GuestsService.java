package com.robot.hotel.service;

import com.robot.hotel.domain.Guests;
import com.robot.hotel.domain.Reservations;
import com.robot.hotel.dto.GuestsDto;
import com.robot.hotel.exception.DublicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.repository.GuestsRepository;
import com.robot.hotel.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestsService {
    private final GuestsRepository guestsRepository;
    private final ReservationsRepository reservationsRepository;


    public List<GuestsDto> findAll() {
        return guestsRepository.findAll().stream()
                .map(this::buildGuestsDto)
                .collect(Collectors.toList());
    }

    public List<GuestsDto> findReservation(Long id) {
        return guestsRepository.findGuestsByReservationsId(id).stream()
                .map(this::buildGuestsDto)
                .collect(Collectors.toList());
    }

    private GuestsDto buildGuestsDto(Guests guest) {
        List<Reservations> reservationsByGuestsId = reservationsRepository.findReservationsByGuestsId(guest.getId());

        return GuestsDto.builder()
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

    private Function<Reservations, String> getReservationsString() {
        return reservations -> "Id:" + reservations.getId().toString()
                + ", room:" + reservations.getRoom().getNumber()
                + ", " + reservations.getCheckInDate().toString() + " - "
                + reservations.getCheckOutDate().toString();
    }

    public void save(GuestsDto guestsDto) throws DublicateObjectException {
        if (findByEmail(guestsDto.getEmail()).isPresent()) {
            throw new DublicateObjectException("Guest with such email is already exists");
        }
        if (findByTelNumber(guestsDto.getTelNumber()).isPresent()) {
            throw new DublicateObjectException("Guest with such tel. number is already exists");
        }
        if (findByPassportSerialNumber(guestsDto.getPassportSerialNumber()).isPresent()) {
            throw new DublicateObjectException("Guest with such passport is already exists");
        }

        Guests guest = buildGuest(guestsDto);
        guestsRepository.save(guest);
    }

    private Guests buildGuest(GuestsDto guestsDto) {
        return Guests.builder()
                .firstName(guestsDto.getFirstName().toLowerCase())
                .lastName(guestsDto.getLastName().toLowerCase())
                .telNumber(guestsDto.getTelNumber())
                .email(guestsDto.getEmail().toLowerCase())
                .passportSerialNumber(guestsDto.getPassportSerialNumber().toLowerCase())
                .build();
    }

    public Optional<GuestsDto> findById(Long id) {
        return guestsRepository.findById(id).map(this::buildGuestsDto);
    }

    public Optional<GuestsDto> findByEmail(String email) {
        return guestsRepository.findGuestsByEmail(email.toLowerCase()).map(this::buildGuestsDto);
    }

    public Optional<GuestsDto> findByTelNumber(String telNumber) {
        return guestsRepository.findGuestsByTelNumber(telNumber.toLowerCase()).map(this::buildGuestsDto);
    }

    public Optional<GuestsDto> findByPassportSerialNumber(String passportSerialNumber) {
        return guestsRepository.findGuestsByPassportSerialNumber(passportSerialNumber.toLowerCase()).map(this::buildGuestsDto);
    }

    public List<GuestsDto> findByLastName(String lastName) {
        return guestsRepository.findGuestsByLastName(lastName).stream()
                .map(this::buildGuestsDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, GuestsDto newGuestsDto) throws NoSuchElementException, DublicateObjectException {
        Optional<GuestsDto> optionalGuestsDto = findById(id);
        GuestsDto guestsDto = null;

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

        Guests guest = buildGuest(guestsDto);
        guest.setId(id);
        guestsRepository.save(guest);
    }

    public void deleteById(Long id) throws NoSuchElementException, NotEmptyObjectException {
        if(findById(id).isEmpty()){
            throw new NoSuchElementException("Such guest is not exists");
        }

        if (reservationsRepository.findReservationsByGuestsId(id).isEmpty()) {
            guestsRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException("This guest has reservations. At first delete reservations.");
        }
    }
}
