package com.robot.hotel.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findReservationsByGuestsId(Long guestsId);

    List<ReservationEntity> findReservationsByRoomId(Long roomId);

    @Query(nativeQuery = true, value = "select distinct room_id from reservation where ?1 >= check_out_date or ?2 <= check_in_date")
    List<Long> findAvailableRooms (LocalDate checkIn, LocalDate checkOut);

    @Query(nativeQuery = true, value = "select * from reservation where ?1 <= check_out_date")
    List<ReservationEntity> findCurrentReservations (LocalDate now);
}
