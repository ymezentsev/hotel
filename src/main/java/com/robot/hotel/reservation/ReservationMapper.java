package com.robot.hotel.reservation;

import com.robot.hotel.config.MapperConfig;
import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationDtoWithoutUserInfo;
import com.robot.hotel.room.RoomMapper;
import com.robot.hotel.user.mapper.UserWithoutReservationsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = {RoomMapper.class, UserWithoutReservationsMapper.class})
public interface ReservationMapper {
    ReservationDto toDto(Reservation reservation);

    @Mappings({
            @Mapping(target = "numberOfGuests", expression = "java(reservation.getUsers().size())"),
    })
    ReservationDtoWithoutUserInfo toDtoWithoutUserInfo(Reservation reservation);
}