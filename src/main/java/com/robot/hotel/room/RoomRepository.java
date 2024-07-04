package com.robot.hotel.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    boolean existsByNumber(String number);

    Optional<Room> findByNumber(String number);

    @Query("""
            SELECT DISTINCT r FROM Room r
            LEFT JOIN FETCH r.reservations res
            WHERE r.id = :id""")
    Optional<Room> findById(Long id);

    Page<Room> findByRoomTypeId(Long id, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.price >= :price")
    Page<Room> findByPriceMoreThanOrEqual(@Param("price") BigDecimal price, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.price <= :price")
    Page<Room> findByPriceLessThanOrEqual(@Param("price") BigDecimal price, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.maxCountOfGuests >= :guestsCount")
    Page<Room> findByGuestsCount(@Param("guestsCount") int guestsCount, Pageable pageable);

    @Query("SELECT r FROM Room r LEFT JOIN Reservation res ON r.id = res.room.id WHERE room.id IS NULL")
    List<Room> findRoomsWithoutReservations();
}