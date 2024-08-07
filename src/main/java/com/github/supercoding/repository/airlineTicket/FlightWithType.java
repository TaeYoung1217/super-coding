package com.github.supercoding.repository.airlineTicket;

import com.github.supercoding.repository.flight.Flight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightWithType {
    private Integer flightId;
    private LocalDateTime departAt;
    private LocalDateTime arrivalAt;
    private String departureLocation;
    private String arrivalLocation;

    public FlightWithType(Flight flight) {
        this.flightId = flight.getFlightId();
        this.departAt = flight.getDepartAt();
        this.arrivalAt = flight.getArrivalAt();
        this.departureLocation = flight.getDepartureLocation();
        this.arrivalLocation = flight.getArrivalLocation();
    }
}
