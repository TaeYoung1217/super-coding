package com.github.supercoding.service.mapper;

import com.github.supercoding.repository.airlineTicket.FlightWithType;
import com.github.supercoding.repository.flight.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {
    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    FlightWithType flightToFlightWithType(Flight flight);
}
