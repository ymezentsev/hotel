package com.robot.hotel.email;

import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

//TODO add test
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailReservationServiceImpl implements EmailReservationService {
    private final EmailSenderService emailSenderService;
    private final EmailContentBuilderService emailContentBuilderService;

    private final ReservationRepository reservationRepository;


    @Override
    public void sendReservationConfirmationEmail(Reservation reservation) {
        reservation.getUsers().forEach(user -> {
            log.info("Sending reservation confirmation email to: {}", user.getEmail());

            emailSenderService.send(
                    user.getEmail().toLowerCase(),
                    emailContentBuilderService.buildEmailContent(user.getFirstName(),
                            null,
                            reservation,
                            EmailSubject.RESERVATION_CONFIRMATION),
                    EmailSubject.RESERVATION_CONFIRMATION.getSubject());
            log.info("Reservation confirmation email sent successfully to: {}", user.getEmail());
        });
    }

    @Override
    public void sendReservationCanceledEmail(Reservation reservation) {
        reservation.getUsers().forEach(user -> {
            log.info("Sending reservation canceled email to: {}", user.getEmail());

            emailSenderService.send(
                    user.getEmail().toLowerCase(),
                    emailContentBuilderService.buildEmailContent(user.getFirstName(),
                            null,
                            reservation,
                            EmailSubject.RESERVATION_CANCELED),
                    EmailSubject.RESERVATION_CONFIRMATION.getSubject());
            log.info("Reservation canceled email sent successfully to: {}", user.getEmail());
        });
    }


    @Override
    @Async
    //cron = "@hourly"
    //"*/30 * * * * *"
    @Scheduled(cron = "0 0 14 * * *")
    public void sendReservationReminder() {
        log.info("Launching reservation reminder");

        reservationRepository.findAll().forEach(reservation -> {
            if (reservation.getCheckInDate().compareTo(ChronoLocalDate.from(LocalDateTime.now().plusDays(2))) == 0) {
                reservation.getUsers().forEach(user -> {
                    log.info("Sending reservation reminder email to: {}", user.getEmail());
                    emailSenderService.send(
                            user.getEmail().toLowerCase(),
                            emailContentBuilderService.buildEmailContent(user.getFirstName(),
                                    null,
                                    reservation,
                                    EmailSubject.RESERVATION_REMINDER),
                            EmailSubject.RESERVATION_REMINDER.getSubject());
                    log.info("Reservation reminder email sent successfully to: {}", user.getEmail());
                });
            }
        });
    }
}