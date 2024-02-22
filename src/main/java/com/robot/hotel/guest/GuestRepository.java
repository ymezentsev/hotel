package com.robot.hotel.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    boolean existsByEmail(String email);

    boolean existsByTelNumber(String telNumber);

    boolean existsByPassportSerialNumber(String passportSerialNumber);

    Optional<Guest> findByEmail(String email);

    @Query("SELECT g FROM Guest g WHERE g.telNumber like :telNumber")
    List<Guest> findByTelNumber(@Param("telNumber") String telNumber);

    Optional<Guest> findByPassportSerialNumber(String passportSerialNumber);

    List<Guest> findByLastName(String lastName);

    Set<Guest> findByReservationsId(Long reservationId);
}
