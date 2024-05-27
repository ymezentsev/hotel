package com.robot.hotel.reservation;

import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationService {
    Page<ReservationDto> findAll(Pageable pageable);

    ReservationDto findById(Long id);

    Page<ReservationDto> findReservationsByUserId(Long userId, Pageable pageable);

    Page<ReservationDto> findReservationsByRoom(String roomNumber, Pageable pageable);

    ReservationDto save(ReservationRequest reservationRequest);

    Page<ReservationDto> findCurrentReservations(Pageable pageable);

    Page<ReservationDto> findCurrentReservationsForSpecificRoom(String roomNumber, Pageable pageable);

    void deleteById(Long id);
}
