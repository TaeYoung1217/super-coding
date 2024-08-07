package com.github.supercoding.web.controller;

import com.github.supercoding.repository.airlineTicket.FlightWithType;
import com.github.supercoding.repository.flight.Flight;
import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.web.dto.airline.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/api/air-reservation")
@RequiredArgsConstructor
@Slf4j
public class AirReservationController {

    private final AirReservationService airReservationService;

    @Operation(summary = "선호하는 ticket 탐색")
    @GetMapping("/tickets")
    public TicketResponse findAirlineTickets(
            @Parameter(name = "user-Id", description = "유저 ID", example = "1") @RequestParam("user-Id") Integer userId,
            @Parameter(name = "airline-ticket-type", description = "항공권 타입", example = "왕복|편도") @RequestParam("airline-ticket-type") String ticketType )
    {
        List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId, ticketType);
        return new TicketResponse(tickets);
    }
    @Operation(summary = "User와 Ticket Id로 예약 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reservations")
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest){
        return airReservationService.makeReservation(reservationRequest);
    }

    @Operation(summary = "userId의 예약한 항공편과 수수료 총합")
    @GetMapping("/users-sum-price")
    public Double findUserFlightSumPrice(@Parameter(name = "user-Id", description = "유저 ID", example = "1") @RequestParam("user-id") Integer userId){
        return airReservationService.findUserFlightSumPrice(userId);
    }

    @Operation(summary = "현재 예약 목록")
    @GetMapping("/reservations")
    public List<ReservationView> findUserReservations(){
        return airReservationService.findAllReservations();

    }

    @Operation(summary = "항공편 타입에 따라 pagination")
    @GetMapping("/flight-pageable")
    public Page<FlightWithType> findFlightWithType(@RequestParam("type")String types, Pageable pageable){
        return airReservationService.findFlightWithType(types,pageable);
    }

    @Operation(summary = "유저 이름으로 예약된 목록 중에서 목적지 리스트 검색")
    @GetMapping("/username-arrival-location")
    public Set<String> findArrivalLocationByUserName(@RequestParam("username") String username){
        return airReservationService.findArrivalLocationByUserName(username);
    }

}