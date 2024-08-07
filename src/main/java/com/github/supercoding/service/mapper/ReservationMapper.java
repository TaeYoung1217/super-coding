package com.github.supercoding.service.mapper;

import com.github.supercoding.repository.reservation.Reservation;
import com.github.supercoding.web.dto.airline.ReservationView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "passenger.passengerId", target = "passengerId")
    @Mapping(source = "airlineTicket.ticketId", target = "airlineTicketId")
    @Mapping(source = "reserveAt", target = "reservationAt")
    ReservationView ReservationToReservationView(Reservation reservation);
}
