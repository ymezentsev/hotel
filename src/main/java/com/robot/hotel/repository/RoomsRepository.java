package com.robot.hotel.repository;

import com.robot.hotel.domain.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long> {

    Optional<Rooms> findRoomsByNumber(String number);
    List<Rooms> findRoomsByRoomTypeId(Long id);

    @Query(nativeQuery = true, value = "select * from rooms where price >= ?1")
    List<Rooms> findRoomsByPriceMoreThan(BigDecimal sum);

    @Query(nativeQuery = true, value = "select * from rooms where price <= ?1")
    List<Rooms> findRoomsByPriceLessThan(BigDecimal sum);
    @Query(nativeQuery = true, value = "select * from rooms where max_count_of_guests >= ?1")
    List<Rooms> findRoomsByGuestsCount(int guestsCount);

}
