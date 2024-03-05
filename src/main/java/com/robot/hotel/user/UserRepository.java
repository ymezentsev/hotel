package com.robot.hotel.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByTelNumber(String telNumber);

    boolean existsByPassportSerialNumber(String passportSerialNumber);

    @Override
    @EntityGraph(attributePaths = {"reservations"})
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = {"reservations"})
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"reservations"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"reservations"})
    Optional<User> findByTelNumber(String telNumber);

    @EntityGraph(attributePaths = {"reservations"})
    Optional<User> findByPassportSerialNumber(String passportSerialNumber);

    @EntityGraph(attributePaths = {"reservations"})
    List<User> findByLastName(String lastName);

    @EntityGraph(attributePaths = {"reservations"})
    List<User> findByReservationsId(Long reservationId);
}