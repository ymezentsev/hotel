package com.robot.hotel.reservation;

import java.util.List;

public interface ReservationService {
    List<ReservationDto> findAll();

    ReservationDto findById(Long id);

    List<ReservationDto> findReservationsByUserId(Long userId);

    List<ReservationDto> findReservationsByRoom(String roomNumber);

    ReservationDto save(ReservationRequest reservationRequest);

    List<ReservationDto> findCurrentReservations();

    List<ReservationDto> findCurrentReservationsForSpecificRoom(String roomNumber);

    void deleteById(Long id);
}
