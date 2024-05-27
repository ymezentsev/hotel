package com.robot.hotel.reservation;

import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {
    Page<ReservationDto> findAll(Pageable pageable);

    ReservationDto findById(Long id);

    List<ReservationDto> findReservationsByUserId(Long userId);

    List<ReservationDto> findReservationsByRoom(String roomNumber);

    ReservationDto save(ReservationRequest reservationRequest);

    List<ReservationDto> findCurrentReservations();

    List<ReservationDto> findCurrentReservationsForSpecificRoom(String roomNumber);

    void deleteById(Long id);
}
