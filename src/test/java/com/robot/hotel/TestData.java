package com.robot.hotel;

import com.robot.hotel.guest.Guest;
import com.robot.hotel.reservation.Reservation;
import com.robot.hotel.roomtype.RoomType;
import com.robot.hotel.room.Room;
import com.robot.hotel.guest.GuestDto;
import com.robot.hotel.reservation.ReservationDto;
import com.robot.hotel.roomtype.RoomTypeDto;
import com.robot.hotel.room.RoomDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TestData {
    public static final Long ROOM_TYPE_ID1 = 1L;
    public static final String ROOM_TYPE_TYPE1 = "standart";
    public static final Long ROOM_TYPE_ID2 = 2L;
    public static final String ROOM_TYPE_TYPE2 = "lux";



    public static final Long ROOM_ID1 = 1L;
    public static final String ROOM_NUMBER1 = "101";
    public static final BigDecimal ROOM_PRICE = BigDecimal.valueOf(1000);
    public static final int ROOM_MAX_COUNT_OF_GUESTS = 2;
    public static RoomType ROOM_TYPE1 = getRoomType1();
    public static final Long ROOM_ID2 = 2L;
    public static final String ROOM_NUMBER2 = "102";
    public static RoomType ROOM_TYPE2 = getRoomType2();



    public static final Long GUEST_ID1 = 1L;
    public static final String GUEST_FIRST_NAME = "Dmitriy";
    public static final String GUEST_LAST_NAME = "Nikolaev";
    public static final String GUEST_TEL_NUMBER = "0961111111";
    public static final String GUEST_EMAIL = "nik@Gmail.com";
    public static final String GUEST_PASSPORT_SERIAL_NUMBER = "BA112233";



    public static final Long RESERVATION_ID1 = 1L;
    public static final LocalDate RESERVATION_CHECK_IN_DATE = LocalDate.of(2023, 8, 15);
    public static final LocalDate RESERVATION_CHECK_OUT_DATE = LocalDate.of(2023, 8, 18);



    public static RoomType getRoomType1() {
        return RoomType.builder()
                .id(ROOM_TYPE_ID1)
                .type(ROOM_TYPE_TYPE1)
                .rooms(List.of(getRoom1()))
                .build();
    }

    public static RoomType getRoomType2() {
        return RoomType.builder()
                .id(ROOM_TYPE_ID2)
                .type(ROOM_TYPE_TYPE2)
                .rooms(List.of(getRoom2()))
                .build();
    }

    public static Room getRoom1() {
        return Room.builder()
                .id(ROOM_ID1)
                .number(ROOM_NUMBER1)
                .price(ROOM_PRICE)
                .maxCountOfGuests(ROOM_MAX_COUNT_OF_GUESTS)
                .roomType(ROOM_TYPE1)
                .reservations(List.of())
                .build();
    }

    public static Room getRoom2() {
        return Room.builder()
                .id(ROOM_ID2)
                .number(ROOM_NUMBER2)
                .price(ROOM_PRICE)
                .maxCountOfGuests(ROOM_MAX_COUNT_OF_GUESTS)
                .roomType(ROOM_TYPE2)
                .reservations(List.of())
                .build();
    }

    public static RoomTypeDto getRoomTypeDto1() {
        return RoomTypeDto.builder()
                .id(ROOM_TYPE_ID1)
                .type(ROOM_TYPE_TYPE1)
                .rooms(List.of(ROOM_NUMBER1))
                .build();
    }

    public static RoomDto getRoomDto1() {
        return RoomDto.builder()
                .id(ROOM_ID1)
                .number(ROOM_NUMBER1)
                .price(ROOM_PRICE)
                .maxCountOfGuests(ROOM_MAX_COUNT_OF_GUESTS)
                .roomType(ROOM_TYPE_TYPE1)
                .reservations(List.of())
                .build();
    }

    public static Guest getGuest1() {
        return Guest.builder()
                .id(GUEST_ID1)
                .firstName(GUEST_FIRST_NAME)
                .lastName(GUEST_LAST_NAME)
                .telNumber(GUEST_TEL_NUMBER)
                .email(GUEST_EMAIL)
                .passportSerialNumber(GUEST_PASSPORT_SERIAL_NUMBER)
                .reservations(Set.of(getReservation1()))
                .build();
    }

    public static GuestDto getGuestDto1() {
        return GuestDto.builder()
                .id(GUEST_ID1)
                .firstName(GUEST_FIRST_NAME)
                .lastName(GUEST_LAST_NAME)
                .telNumber(GUEST_TEL_NUMBER)
                .email(GUEST_EMAIL)
                .passportSerialNumber(GUEST_PASSPORT_SERIAL_NUMBER)
                .build();
    }

    public static Reservation getReservation1() {
        return Reservation.builder()
                .id(RESERVATION_ID1)
                .room(getRoom1())
                .checkInDate(RESERVATION_CHECK_IN_DATE)
                .checkOutDate(RESERVATION_CHECK_OUT_DATE)
                .build();
    }

    public static ReservationDto getReservationDto1() {
        return ReservationDto.builder()
                .id(RESERVATION_ID1)
                .roomNumber(ROOM_NUMBER1)
                .checkInDate(RESERVATION_CHECK_IN_DATE)
                .checkOutDate(RESERVATION_CHECK_OUT_DATE)
                .guests(Set.of())
                .build();
    }
}
