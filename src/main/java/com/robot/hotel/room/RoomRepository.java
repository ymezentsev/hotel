package com.robot.hotel.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    boolean existsByNumber(String number);

    Optional<Room> findByNumber(String number);

    @Override
    @NonNull
    @Query("""
            SELECT DISTINCT r FROM Room r
            LEFT JOIN FETCH r.reservations res
            WHERE r.id = :id""")
    Optional<Room> findById(@NonNull Long id);

    @Query("""
            SELECT rm FROM Room rm
            LEFT JOIN FETCH rm.reservations r
            WHERE rm NOT IN (
                SELECT DISTINCT r.room FROM Reservation r
                WHERE ((r.checkInDate <= :checkIn AND :checkIn < r.checkOutDate)
                OR (r.checkInDate < :checkOut AND :checkOut <= r.checkOutDate)
                OR (:checkIn <= r.checkInDate AND r.checkOutDate <= :checkOut))
                AND r.checkOutDate > CURDATE()
            )
            """)
    List<Room> findFreeRooms(@Param("checkIn") LocalDate checkIn,
                             @Param("checkOut") LocalDate checkOut);
}