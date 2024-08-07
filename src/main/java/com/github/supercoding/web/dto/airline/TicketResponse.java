package com.github.supercoding.web.dto.airline;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TicketResponse {
    private List<Ticket> tickets;

    public TicketResponse(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
