package atu.ie.bookingservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private double price;
    private int availableTickets;
    private Long venueId;
}