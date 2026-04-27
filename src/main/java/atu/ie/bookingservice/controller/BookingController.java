package atu.ie.bookingservice.controller;

import atu.ie.bookingservice.model.Booking;
import atu.ie.bookingservice.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/buy")
    public ResponseEntity<Booking> buyTickets(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(bookingService.buyTickets(userId, eventId, quantity));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }
}