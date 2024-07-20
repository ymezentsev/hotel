package com.robot.hotel;

import com.robot.hotel.reservation.ReservationRepository;
import com.robot.hotel.room.RoomRepository;
import com.robot.hotel.roomtype.RoomTypeRepository;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestDBUtils {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final  ReservationRepository reservationRepository;

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

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow();
    }

    public Long getReservationIdByRoom(String number) {
        return reservationRepository.findByRoomId(getRoomIdByNumber(number), Pageable.unpaged())
                .getContent()
                .get(0)
                .getId();
    }
}