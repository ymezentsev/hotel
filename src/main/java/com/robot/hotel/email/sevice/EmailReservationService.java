package com.robot.hotel.email.sevice;

import com.robot.hotel.reservation.Reservation;

public interface EmailReservationService {
    void sendReservationConfirmationEmail(Reservation reservation);
    void sendReservationCanceledEmail(Reservation reservation);
    void sendReservationReminder();
}
