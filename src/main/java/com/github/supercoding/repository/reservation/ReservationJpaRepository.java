package com.github.supercoding.repository.reservation;

import com.github.supercoding.repository.flight.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT new com.github.supercoding.repository.reservation.FlightPriceAndCharge(f.flightPrice, f.charge) " +
            "FROM Reservation r " +
            "JOIN r.passenger p " +
            "JOIN r.airlineTicket a " +
            "JOIN a.flightList f " +
            "WHERE p.user.userId = :userId ")
    List<FlightPriceAndCharge> findFlightPriceAndCharge(Integer userId);

    @Query("SELECT r " +
            "FROM Reservation r " +
            "JOIN FETCH r.airlineTicket a " +
            "JOIN FETCH a.flightList f " +
            "WHERE r.passenger.user.userName = :userName")
    Set<Reservation> findAllByPassenger_User_UserName(String userName);

}