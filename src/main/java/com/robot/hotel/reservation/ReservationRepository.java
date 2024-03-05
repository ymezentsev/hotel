package com.robot.hotel.reservation;

import com.robot.hotel.room.Room;
import org.springframework.data.jpa.repository.EntityGraph;
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
    @EntityGraph(attributePaths = {"users"})
    List<Reservation> findAll();

    @Override
    @EntityGraph(attributePaths = {"users"})
    Optional<Reservation> findById(Long id);

    @EntityGraph(attributePaths = {"users"})
    List<Reservation> findByUsersId(Long userId);

    @EntityGraph(attributePaths = {"users"})
    List<Reservation> findByRoomId(Long roomId);

    @Query("SELECT distinct r.room FROM Reservation r WHERE (r.checkOutDate <= :checkIn or r.checkInDate >= :checkOut)" +
            "and r.checkOutDate > CURDATE()")
    List<Room> findFreeRoomsWithReservations(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    @EntityGraph(attributePaths = {"users"})
    @Query("SELECT r FROM Reservation r WHERE r.checkOutDate >= CURDATE()")
    List<Reservation> findCurrentReservations();

    @EntityGraph(attributePaths = {"users"})
    @Query("SELECT r FROM Reservation r WHERE r.checkOutDate >= CURDATE() and r.room.id = :roomId")
    List<Reservation> findCurrentReservationsForRoom(@Param("roomId") Long roomId);
}
