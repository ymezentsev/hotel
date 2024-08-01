package com.robot.hotel.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT r FROM Room r LEFT JOIN Reservation res ON r.id = res.room.id WHERE room.id IS NULL")
    List<Room> findRoomsWithoutReservations();
}