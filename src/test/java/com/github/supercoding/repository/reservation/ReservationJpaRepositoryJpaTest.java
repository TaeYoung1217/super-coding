package com.github.supercoding.repository.reservation;

import com.github.supercoding.repository.airlineTicket.AirlineTicket;
import com.github.supercoding.repository.airlineTicket.AirlineTicketJpaRepository;
import com.github.supercoding.repository.flight.Flight;
import com.github.supercoding.repository.passenger.Passenger;
import com.github.supercoding.repository.passenger.PassengerJpaRepository;
import com.github.supercoding.service.AirReservationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //slice test -> Jpa를 사용하는 Dao Layer를 호출하는 테스트
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class ReservationJpaRepositoryJpaTest {

//    @Autowired
//    private AirReservationService airReservationService;
//
    @Autowired
    private PassengerJpaRepository passengerJpaRepository;
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;
    @Autowired
    private AirlineTicketJpaRepository airlineTicketJpaRepository;


    @DisplayName("ReservationJpaRepository로 항공편 가격, 수수료 검색")
    @Test
    void findFlightPriceAndCharge() {
        //given
        Integer userId = 1;

        //when
        List<FlightPriceAndCharge> flightPriceAndCharges = reservationJpaRepository.findFlightPriceAndCharge(userId);

        //then
        log.info("flightPriceAndCharges: {}", flightPriceAndCharges);
    }
    @DisplayName("Reservation 진행")
    @Test
    void saveReservation() {
        //given
        Integer userId = 10;
        Integer ticketId = 2;

        Passenger passenger = passengerJpaRepository.findById(userId).orElse(null);
        AirlineTicket airlineTicket = airlineTicketJpaRepository.findById(ticketId).orElse(null);

        //when
        Reservation reservation = new Reservation(passenger, airlineTicket);
        Reservation reservation1 = reservationJpaRepository.save(reservation);

        //then
        log.info(reservation1.toString());
        assertEquals(reservation1.getPassenger(), passenger);
        assertEquals(reservation1.getAirlineTicket(), airlineTicket);

    }

}