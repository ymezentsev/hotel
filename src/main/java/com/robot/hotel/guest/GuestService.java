package com.robot.hotel.guest;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    private static final String GUEST_IS_ALREADY_EXISTS = "Guest with such %s is already exists";
    private static final String GUEST_IS_NOT_EXISTS = "Such guest is not exists";
    private static final String RESERVATIONS_FOR_THIS_GUEST_ARE_EXISTS =
            "This guest has reservations. At first delete reservations";

    public List<GuestDto> findAll() {
        return guestRepository.findAll().stream()
                .map(guestMapper::buildGuestsDto)
                .toList();
    }

    public GuestDto save(GuestRequest guestRequest) {
        guestRequest.setTelNumber(updateTelNumber(guestRequest.getTelNumber()));

        if (guestRepository.existsByEmail(guestRequest.getEmail().toLowerCase().strip())) {
            throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "email"));
        }

        if (guestRepository.existsByTelNumber(guestRequest.getTelNumber())) {
            throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "tel.number"));
        }

        if (Objects.nonNull(guestRequest.getPassportSerialNumber()) && !guestRequest.getPassportSerialNumber().isBlank()) {
            if (guestRepository.existsByPassportSerialNumber(
                    guestRequest.getPassportSerialNumber().toLowerCase().strip())) {
                throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "passport"));
            }
        }

        Guest newGuest = guestMapper.buildGuestFromRequest(guestRequest);
        return guestMapper.buildGuestsDto(guestRepository.save(newGuest));
    }

    public GuestDto findById(Long id) {
        return guestMapper.buildGuestsDto(guestRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(GUEST_IS_NOT_EXISTS)));
    }

    public GuestDto findByEmail(String email) {
        return guestMapper.buildGuestsDto(guestRepository
                .findByEmail(email.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(GUEST_IS_NOT_EXISTS)));
    }

    public GuestDto findByTelNumber(String telNumber) {
        return guestMapper.buildGuestsDto(guestRepository
                .findByTelNumber(updateTelNumber(telNumber))
                .orElseThrow(() -> new NoSuchElementException(GUEST_IS_NOT_EXISTS)));
    }

    public GuestDto findByPassportSerialNumber(String passportSerialNumber) {
        return guestMapper.buildGuestsDto(guestRepository
                .findByPassportSerialNumber(passportSerialNumber.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(GUEST_IS_NOT_EXISTS)));
    }

    public List<GuestDto> findByLastName(String lastName) {
        return guestRepository.findByLastName(lastName.toLowerCase().strip()).stream()
                .map(guestMapper::buildGuestsDto)
                .toList();
    }

    public List<GuestDto> findGuestByReservation(Long id) {
        return guestRepository.findByReservationsId(id).stream()
                .map(guestMapper::buildGuestsDto)
                .toList();
    }

    public void update(Long id, GuestRequest guestRequest) {
        Guest guestToUpdate = guestRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(GUEST_IS_NOT_EXISTS)
        );

        guestRequest.setTelNumber(updateTelNumber(guestRequest.getTelNumber()));

        Optional<Guest> existingGuest = guestRepository.findByEmail(guestRequest.getEmail().toLowerCase().strip());
        if (existingGuest.isPresent() && !Objects.equals(existingGuest.get().getId(), id)) {
            throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "email"));
        }

        existingGuest = guestRepository.findByTelNumber(guestRequest.getTelNumber());
        if (existingGuest.isPresent() && !Objects.equals(existingGuest.get().getId(), id)) {
            throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "tel.number"));
        }

        if (Objects.nonNull(guestRequest.getPassportSerialNumber()) && !guestRequest.getPassportSerialNumber().isBlank()) {
            existingGuest = guestRepository.findByPassportSerialNumber(
                    guestRequest.getPassportSerialNumber().toLowerCase().strip());
            if (existingGuest.isPresent() && !Objects.equals(existingGuest.get().getId(), id)) {
                throw new DuplicateObjectException(String.format(GUEST_IS_ALREADY_EXISTS, "passport"));
            }
        } else {
            guestRequest.setPassportSerialNumber("");
        }

        guestToUpdate.setFirstName(guestRequest.getFirstName().strip());
        guestToUpdate.setLastName(guestRequest.getLastName().strip());
        guestToUpdate.setTelNumber(guestRequest.getTelNumber().strip());
        guestToUpdate.setEmail(guestRequest.getEmail().strip());
        guestToUpdate.setPassportSerialNumber(guestRequest.getPassportSerialNumber().strip());
        guestRepository.save(guestToUpdate);
    }

    public void deleteById(Long id) {
        if (guestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(GUEST_IS_NOT_EXISTS))
                .getReservations()
                .isEmpty()) {
            guestRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException(RESERVATIONS_FOR_THIS_GUEST_ARE_EXISTS);
        }
    }

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
