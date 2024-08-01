package com.robot.hotel.roomtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findByType(String type);

    boolean existsByType(String type);

    @Override
    @NonNull
    @Query("""
            SELECT DISTINCT rt FROM RoomType rt
            LEFT JOIN FETCH rt.rooms r
            WHERE rt.id = :id""")
    Optional<RoomType> findById(@NonNull Long id);
}