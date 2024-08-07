package com.github.supercoding.repository.reservation;

public interface ReservationRepository {
    Boolean saveReservation(Reservation reservation);

    Reservation findReservationWithPassengerIdAndAirlineTicketId(Integer passengerId, Integer airlineTicketId);

    void updateReservationStatus(Integer reservationId, String status);
}
