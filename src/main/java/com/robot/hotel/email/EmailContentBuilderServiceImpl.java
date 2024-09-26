package com.robot.hotel.email;

import com.robot.hotel.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailContentBuilderServiceImpl implements EmailContentBuilderService {
    private final TemplateEngine htmlTemplateEngine;

    @Override
    public String buildEmailContent(String name, String token, Reservation reservation, EmailSubject subject) {
        String link = subject.getLink(token);
        String template = subject.getTemplate();

        Context ctx = new Context();
        ctx.setVariable("name", name);
        if (token != null) {
            ctx.setVariable("url", link);
        }

        if (reservation != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, EEE").withLocale(Locale.US);
            BigDecimal totalPrice = reservation.getRoom().getPrice()
                    .multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate())));

            ctx.setVariable("checkIn", reservation.getCheckInDate().format(dateTimeFormatter) + " (14:00)");
            ctx.setVariable("checkOut", reservation.getCheckOutDate().format(dateTimeFormatter) + " (12:00)");
            ctx.setVariable("room", "№" + reservation.getRoom().getNumber()
                    + ", " + reservation.getRoom().getRoomType().getType());
            ctx.setVariable("guestsQuantity", reservation.getUsers().size());
            ctx.setVariable("price", reservation.getRoom().getPrice());
            ctx.setVariable("totalPrice", totalPrice);
        }
        return htmlTemplateEngine.process(template, ctx);
    }
}
