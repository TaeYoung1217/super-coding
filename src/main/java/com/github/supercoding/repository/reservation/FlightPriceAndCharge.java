package com.github.supercoding.repository.reservation;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlightPriceAndCharge {
    private Double flightPrice;
    private Double charge;
}