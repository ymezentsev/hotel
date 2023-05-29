package com.robot.hotel.repository;

import com.robot.hotel.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByGuestsId(Long guestsId);

    List<Reservation> findReservationsByRoomId(Long roomId);

    @Query(nativeQuery = true, value = "select distinct room_id from reservation where ?1 >= check_out_date or ?2 <= check_in_date")
    List<Long> findAvailableRooms (LocalDate checkIn, LocalDate checkOut);

    @Query(nativeQuery = true, value = "select * from reservation where ?1 <= check_out_date")
    List<Reservation> findCurrentReservations (LocalDate now);
}
