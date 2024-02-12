package com.robot.hotel.guest;

import com.robot.hotel.guest.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findGuestsByEmail(String email);
    Optional<Guest> findGuestsByTelNumber(String telNumber);
    Optional<Guest> findGuestsByPassportSerialNumber(String passportSerialNumber);
    List<Guest> findGuestsByLastName(String lastName);
    Set<Guest> findGuestsByReservationsId(Long reservationId);
}
