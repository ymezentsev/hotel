package com.robot.hotel.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT u FROM User u JOIN u.country c " +
            "WHERE CONCAT(c.telCode, u.telNumber) = :telNumber")
    Optional<User> findByFullTelNumber(@Param("telNumber") String telNumber);

    @EntityGraph(attributePaths = {"reservations"})
    @Query("SELECT u FROM User u JOIN u.passport p " +
            "WHERE p.serialNumber = :passportSerialNumber")
    Optional<User> findByPassportSerialNumber(@Param("passportSerialNumber") String passportSerialNumber);

    @EntityGraph(attributePaths = {"reservations"})
    List<User> findByLastName(String lastName);

    @EntityGraph(attributePaths = {"reservations"})
    List<User> findByReservationsId(Long reservationId);

    @EntityGraph(attributePaths = {"reservations"})
    List<User> findByRole(Role role);
}
