package com.robot.hotel.email.sevice.impl;

import com.robot.hotel.email.sevice.EmailContentBuilderService;
import com.robot.hotel.email.sevice.EmailReservationService;
import com.robot.hotel.email.sevice.EmailSenderService;
import com.robot.hotel.email.EmailSubject;
import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.reservation.ReservationRepository;
import jakarta.transaction.Transactional;
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

    private static final int DAYS_TO_CHECK_IN = 2;

    @Override
    @Async("myAsyncPoolTaskExecutor")
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
    @Async("myAsyncPoolTaskExecutor")
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
    @Async("myAsyncPoolTaskExecutor")
    //Scheduled every day at 14:00
    @Scheduled(cron = "0 0 14 * * *")
    @Transactional
    public void sendReservationReminder() {
        log.info("Launching reservation reminder");

        reservationRepository.findAll().forEach(reservation -> {
            if (reservation.getCheckInDate()
                    .compareTo(ChronoLocalDate.from(LocalDateTime.now().plusDays(DAYS_TO_CHECK_IN))) == 0) {
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