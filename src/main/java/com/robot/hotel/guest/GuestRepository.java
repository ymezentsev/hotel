package com.robot.hotel.guest;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    boolean existsByEmail(String email);

    boolean existsByTelNumber(String telNumber);

    boolean existsByPassportSerialNumber(String passportSerialNumber);

    @Override
    @EntityGraph(attributePaths = {"reservations"})
    List<Guest> findAll();

    @Override
    @EntityGraph(attributePaths = {"reservations"})
    Optional<Guest> findById(Long id);

    @EntityGraph(attributePaths = {"reservations"})
    Optional<Guest> findByEmail(String email);

    @EntityGraph(attributePaths = {"reservations"})
    Optional<Guest> findByTelNumber(String telNumber);

    @EntityGraph(attributePaths = {"reservations"})
    Optional<Guest> findByPassportSerialNumber(String passportSerialNumber);

    @EntityGraph(attributePaths = {"reservations"})
    List<Guest> findByLastName(String lastName);

    @EntityGraph(attributePaths = {"reservations"})
    List<Guest> findByReservationsId(Long reservationId);
}
