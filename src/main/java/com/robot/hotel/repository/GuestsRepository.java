package com.robot.hotel.repository;

import com.robot.hotel.domain.Guests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GuestsRepository extends JpaRepository<Guests, Long> {
    Optional<Guests> findGuestsByEmail(String email);
    Optional<Guests> findGuestsByTelNumber(String telNumber);
    Optional<Guests> findGuestsByPassportSerialNumber(String passportSerialNumber);
    List<Guests> findGuestsByLastName(String lastName);
    Set<Guests> findGuestsByReservationsId(Long reservationId);
}
