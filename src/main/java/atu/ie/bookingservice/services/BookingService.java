package atu.ie.bookingservice.services;

import atu.ie.bookingservice.dto.EventDto;
import atu.ie.bookingservice.dto.UserDto;
import atu.ie.bookingservice.model.Booking;
import atu.ie.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

    @Value("${services.user.url}")
    private String userServiceUrl;

    @Value("${services.event.url}")
    private String eventServiceUrl;

    public Booking buyTickets(Long userId, Long eventId, int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        UserDto user = getUserFromUserService(userId);
        EventDto event = getEventFromEventService(eventId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        if (event.getAvailableTickets() < quantity) {
            throw new RuntimeException("Not enough tickets available");
        }

        restTemplate.put(
                eventServiceUrl + "/events/" + eventId + "/reserve?quantity=" + quantity,
                null
        );

        double totalPrice = event.getPrice() * quantity;

        Booking booking = Booking.builder()
                .userId(userId)
                .eventId(eventId)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        getUserFromUserService(userId);
        return bookingRepository.findByUserId(userId);
    }

    private UserDto getUserFromUserService(Long userId) {
        try {
            return restTemplate.getForObject(
                    userServiceUrl + "/users/" + userId,
                    UserDto.class
            );
        } catch (RestClientException e) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    private EventDto getEventFromEventService(Long eventId) {
        try {
            return restTemplate.getForObject(
                    eventServiceUrl + "/events/" + eventId,
                    EventDto.class
            );
        } catch (RestClientException e) {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }
    }
}