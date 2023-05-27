package com.robot.hotel.repository;

import com.robot.hotel.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findRoomTypeByType(String type);
}
