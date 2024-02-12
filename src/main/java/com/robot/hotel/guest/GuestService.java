package com.robot.hotel.guest;

import com.robot.hotel.reservation.ReservationEntity;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.reservation.ReservationRepository;
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

    private GuestDto buildGuestsDto(GuestEntity guestEntity) {
        List<ReservationEntity> reservationsByGuestsId = reservationRepository.findReservationsByGuestsId(guestEntity.getId());

        return GuestDto.builder()
                .id(guestEntity.getId())
                .firstName(guestEntity.getFirstName())
                .lastName(guestEntity.getLastName())
                .telNumber(guestEntity.getTelNumber())
                .email(guestEntity.getEmail())
                .passportSerialNumber(guestEntity.getPassportSerialNumber())
                .reservations(reservationsByGuestsId.stream()
                        .map(getReservationsString())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Function<ReservationEntity, String> getReservationsString() {
        return reservations -> "Id:" + reservations.getId().toString()
                + ", room:" + reservations.getRoomEntity().getNumber()
                + ", " + reservations.getCheckInDate().toString() + " - "
                + reservations.getCheckOutDate().toString();
    }

    public GuestEntity save(GuestDto guestsDto) {
        if (findByEmail(guestsDto.getEmail()).isPresent()) {
            throw new DuplicateObjectException("Guest with such email is already exists");
        }
        if (findByTelNumber(guestsDto.getTelNumber()).isPresent()) {
            throw new DuplicateObjectException("Guest with such tel.number is already exists");
        }
        if (findByPassportSerialNumber(guestsDto.getPassportSerialNumber()).isPresent()) {
            throw new DuplicateObjectException("Guest with such passport is already exists");
        }

        GuestEntity guestEntity = buildGuest(guestsDto);
        return guestRepository.save(guestEntity);
    }

    private GuestEntity buildGuest(GuestDto guestsDto) {
        return GuestEntity.builder()
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
        return guestRepository.findGuestsByLastName(lastName.toLowerCase()).stream()
                .map(this::buildGuestsDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, GuestDto newGuestsDto) {
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

        GuestEntity guestEntity = buildGuest(guestsDto);
        guestEntity.setId(id);
        guestRepository.save(guestEntity);
    }

    public void deleteById(Long id) {
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
