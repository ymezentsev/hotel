package com.robot.hotel.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    @NonNull
    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users""")
    Page<Reservation> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.id = :id""")
    Optional<Reservation> findById(@NonNull Long id);

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE u.id = :userId""")
    Page<Reservation> findByUsersId(Long userId, Pageable pageable);

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.room.id = :roomId""")
    Page<Reservation> findByRoomId(Long roomId, Pageable pageable);

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.checkOutDate >= CURDATE()""")
    Page<Reservation> findCurrentReservations(Pageable pageable);

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.checkOutDate >= CURDATE() and r.room.id = :roomId""")
    Page<Reservation> findCurrentReservationsForRoom(@Param("roomId") Long roomId, Pageable pageable);
}