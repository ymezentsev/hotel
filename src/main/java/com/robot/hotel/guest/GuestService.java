package com.robot.hotel.guest;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;
    private final GuestMapper guestMapper;

    private static final String GUEST_IS_ALREADY_EXISTS = "Guest with such %s is already exists";

    public List<GuestDto> findAll() {
        return guestRepository.findAll().stream()
                .map(guestMapper::buildGuestsDto)
                .toList();
    }

    public GuestDto save(GuestRequest guestRequest) {
        String updatedTelNumber = updateTelNumber(guestRequest.getTelNumber());

        if (Boolean.TRUE.equals(guestRepository.existsByEmail(guestRequest.getEmail().toLowerCase()))) {
            throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "email"));
        }

        if (Boolean.TRUE.equals(guestRepository.existsByTelNumber(updatedTelNumber))) {
            throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "tel.number"));
        }

        if (Boolean.TRUE.equals(guestRepository.existsByPassportSerialNumber(
                guestRequest.getPassportSerialNumber().toLowerCase()))) {
            throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "passport"));
        }

        guestRequest.setTelNumber(updatedTelNumber);
        Guest newGuest = guestMapper.buildGuestFromRequest(guestRequest);
        return guestMapper.buildGuestsDto(guestRepository.save(newGuest));
    }

    public GuestDto findById(Long id) {
        return guestMapper.buildGuestsDto(guestRepository
                .findById(id)
                .orElseThrow());
    }

    public GuestDto findByEmail(String email) {
        return guestMapper.buildGuestsDto(guestRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow());
    }

    public List<GuestDto> findByTelNumber(String telNumber) {
        String updatedTelNumber = "%" + updateTelNumber(telNumber);

        return guestRepository.findByTelNumber(updatedTelNumber).stream()
                .map(guestMapper::buildGuestsDto)
                .toList();
    }

    public GuestDto findByPassportSerialNumber(String passportSerialNumber) {
        return guestMapper.buildGuestsDto(guestRepository
                .findByPassportSerialNumber(passportSerialNumber.toLowerCase())
                .orElseThrow());
    }

    public List<GuestDto> findByLastName(String lastName) {
        return guestRepository.findByLastName(lastName.toLowerCase()).stream()
                .map(guestMapper::buildGuestsDto)
                .toList();
    }

    public List<GuestDto> findGuestByReservation(Long id) {
        return guestRepository.findByReservationsId(id).stream()
                .map(guestMapper::buildGuestsDto)
                .toList();
    }

    /*public void update(Long id, GuestDto newGuestsDto) {
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
                throw new DuplicateObjectException("Guest with such tel.number is already exists");
            }
            guestsDto.setTelNumber(newGuestsDto.getTelNumber());
        }
        if (newGuestsDto.getEmail() != null) {
            if (findByEmail(newGuestsDto.getEmail()).isPresent()) {
                throw new DuplicateObjectException("Guest with such email is already exists");
            }
            guestsDto.setEmail(newGuestsDto.getEmail());
        }
        if (newGuestsDto.getPassportSerialNumber() != null) {
            if (findByPassportSerialNumber(newGuestsDto.getPassportSerialNumber()).isPresent()) {
                throw new DuplicateObjectException("Guest with such passport is already exists");
            }
            guestsDto.setPassportSerialNumber(newGuestsDto.getPassportSerialNumber());
        }

        *//*Guest guest = buildGuest(guestsDto);
        guest.setId(id);
        guestRepository.save(guest);*//*
    }

    public void deleteById(Long id) {
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("Such guest is not exists");
        }

        if (reservationRepository.findByGuestsId(id).isEmpty()) {
            guestRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException("This guest has reservations. At first delete reservations.");
        }
    }*/

    private String updateTelNumber(String telNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = telNumber.toCharArray();
        if (chars[0] == '+') {
            stringBuilder.append(chars[0]);
        }

        for (char ch : chars) {
            if (Character.isDigit(ch)) {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }
}
