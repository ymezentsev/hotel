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

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPassportSerialNumber(String passportSerialNumber);

    @Override
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r""")
    List<User> findAll();

    @Override
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE u.id = :id""")
    Optional<User> findById(Long id);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE u.email = :email""")
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE u.phoneNumber = :phoneNumber""")
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            JOIN u.country c
            WHERE CONCAT(c.phoneCode, u.phoneNumber) = :phoneNumber""")
    Optional<User> findByFullPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.reservations r
            JOIN u.passport p
            WHERE p.serialNumber = :passportSerialNumber""")
    Optional<User> findByPassportSerialNumber(@Param("passportSerialNumber") String passportSerialNumber);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE u.lastName = :lastName""")
    List<User> findByLastName(String lastName);

    @EntityGraph(attributePaths = {"reservations"})
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE r.id = :reservationId""")
    List<User> findByReservationsId(Long reservationId);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE u.role = :role""")
    List<User> findByRole(Role role);
}