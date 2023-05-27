package com.robot.hotel.repository;

import com.robot.hotel.domain.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservations, Long> {
    List<Reservations> findReservationsByGuestsId(Long guestsId);

    List<Reservations> findReservationsByRoomId(Long roomId);

    @Query(nativeQuery = true, value = "select distinct room_id from reservations where ?1 >= check_out_date or ?2 <= check_in_date")
    List<Long> findAvailableRooms (LocalDate checkIn, LocalDate checkOut);

    @Query(nativeQuery = true, value = "select * from reservations where ?1 <= check_out_date")
    List<Reservations> findCurrentReservations (LocalDate now);
}
