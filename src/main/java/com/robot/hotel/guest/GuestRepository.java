package com.robot.hotel.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
    Optional<GuestEntity> findGuestsByEmail(String email);
    Optional<GuestEntity> findGuestsByTelNumber(String telNumber);
    Optional<GuestEntity> findGuestsByPassportSerialNumber(String passportSerialNumber);
    List<GuestEntity> findGuestsByLastName(String lastName);
    Set<GuestEntity> findGuestsByReservationsId(Long reservationId);
}
