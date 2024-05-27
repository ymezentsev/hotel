package com.robot.hotel.reservation;

import com.robot.hotel.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users""")
    Page<Reservation> findAll(Pageable pageable);

    @Override
    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.id = :id""")
    Optional<Reservation> findById(Long id);

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE u.id = :userId""")
    List<Reservation> findByUsersId(Long userId);

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.room.id = :roomId""")
    List<Reservation> findByRoomId(Long roomId);

    @Query("""
            SELECT DISTINCT r.room FROM Reservation r
            WHERE (r.checkOutDate <= :checkIn or r.checkInDate >= :checkOut)
            and r.checkOutDate > CURDATE()""")
    List<Room> findFreeRoomsWithReservations(@Param("checkIn") LocalDate checkIn,
                                             @Param("checkOut") LocalDate checkOut);

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.checkOutDate >= CURDATE()""")
    List<Reservation> findCurrentReservations();

    @Query("""
            SELECT DISTINCT r FROM Reservation r
            LEFT JOIN FETCH r.users u
            WHERE r.checkOutDate >= CURDATE() and r.room.id = :roomId""")
    List<Reservation> findCurrentReservationsForRoom(@Param("roomId") Long roomId);
}