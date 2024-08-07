package com.github.supercoding.repository.payments;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(of="paymentId")
@NoArgsConstructor

public class Payment {
    private Integer paymentId;
    private Integer passengerId;
    private Integer reservationId;
    private LocalDateTime payTime;

    public Payment(Integer reservationId, Integer passengerId) {
        this.reservationId = reservationId;
        this.passengerId = passengerId;
        this.payTime = LocalDateTime.now();
    }
}
