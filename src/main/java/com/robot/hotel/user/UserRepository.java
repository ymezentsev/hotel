package com.robot.hotel.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPassportSerialNumber(String passportSerialNumber);

    @Override
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r""")
    Page<User> findAll(Pageable pageable);

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
    Page<User> findByLastName(String lastName, Pageable pageable);

    @EntityGraph(attributePaths = {"reservations"})
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE r.id = :reservationId""")
    Page<User> findByReservationsId(Long reservationId, Pageable pageable);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.reservations r
            WHERE u.role = :role""")
    Page<User> findByRole(Role role, Pageable pageable);
}