package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingGatewayController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Gateway: Creating booking for user ID: {}", userId);
        return bookingClient.create(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        log.info("Gateway: Updating booking ID: {} status to: {}", bookingId, approved);
        return bookingClient.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        log.info("Gateway: Getting booking ID: {} for user ID: {}", bookingId, userId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Gateway: Getting bookings for booker ID: {} with state: {}", userId, state);

        validatePagination(from, size);
        validateState(state);

        return bookingClient.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Gateway: Getting bookings for owner ID: {} with state: {}", userId, state);

        validatePagination(from, size);
        validateState(state);

        return bookingClient.getAllByOwner(userId, state, from, size);
    }

    private void validatePagination(Integer from, Integer size) {
        if (from < 0) {
            throw new IllegalArgumentException("From parameter cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size parameter must be positive");
        }
    }

    private void validateState(String state) {
        if (!state.matches("(?i)ALL|CURRENT|PAST|FUTURE|WAITING|REJECTED")) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}