package com.robot.hotel;

import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.roomtype.RoomTypeRepository;
import com.robot.hotel.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDBUtils {
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    public Long getRoomTypeIdByType(String type) {
        return roomTypeRepository.findByType(type)
                .orElseThrow()
                .getId();
    }

    public Long getRoomIdByNumber(String number) {
        return roomRepository.findByNumber(number)
                .orElseThrow()
                .getId();
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
    }

    public Long getReservationIdByRoom(String number) {
        return reservationRepository.findByRoomId(getRoomIdByNumber(number))
                .get(0)
                .getId();
    }
}
