package com.robot.hotel;

import com.robot.hotel.guest.GuestRepository;
import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.roomtype.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDBUtils {
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    GuestRepository guestRepository;

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

    public Long getGuestIdByEmail(String email) {
        return guestRepository.findByEmail(email)
                .orElseThrow()
                .getId();
    }

    public Long getReservationIdByRoom(String number) {
        return reservationRepository.findByRoomId(getRoomIdByNumber(number))
                .get(0)
                .getId();
    }
}
