package com.robot.hotel.roomtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Long> {
    Optional<RoomTypeEntity> findRoomTypeByType(String type);
}
