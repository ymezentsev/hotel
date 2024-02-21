package com.robot.hotel.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmail(String email);

    Optional<Guest> findByTelNumber(String telNumber);

    Optional<Guest> findByPassportSerialNumber(String passportSerialNumber);

    List<Guest> findByLastName(String lastName);

    Set<Guest> findByReservationsId(Long reservationId);
}
