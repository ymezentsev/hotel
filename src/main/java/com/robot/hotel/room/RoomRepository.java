package com.robot.hotel.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> findRoomsByNumber(String number);
    List<RoomEntity> findRoomsByRoomTypeId(Long id);

    @Query(nativeQuery = true, value = "select * from room where price >= ?1")
    List<RoomEntity> findRoomsByPriceMoreThanOrEqual(BigDecimal sum);

    @Query(nativeQuery = true, value = "select * from room where price <= ?1")
    List<RoomEntity> findRoomsByPriceLessThanOrEqual(BigDecimal sum);
    @Query(nativeQuery = true, value = "select * from room where max_count_of_guests >= ?1")
    List<RoomEntity> findRoomsByGuestsCount(int guestsCount);

}
