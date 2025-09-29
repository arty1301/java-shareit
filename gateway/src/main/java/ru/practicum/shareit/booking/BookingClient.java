package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Map;

@Slf4j
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit.server.url}") String serverUrl, RestTemplate rest) {
        super(rest, serverUrl);
        log.info("BookingClient initialized with server URL: {}", serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, BookingRequestDto bookingRequestDto) {
        log.debug("Sending POST request to create booking for user ID: {}", userId);
        return post(API_PREFIX, userId, bookingRequestDto);
    }

    public ResponseEntity<Object> updateStatus(Long userId, Long bookingId, Boolean approved) {
        log.debug("Sending PATCH request to update booking ID: {} status to: {}", bookingId, approved);
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch(API_PREFIX + "/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getById(Long userId, Long bookingId) {
        log.debug("Sending GET request for booking ID: {} for user ID: {}", bookingId, userId);
        return get(API_PREFIX + "/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllByBooker(Long userId, String state, Integer from, Integer size) {
        log.debug("Sending GET request for bookings of booker ID: {} with state: {}", userId, state);
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByOwner(Long userId, String state, Integer from, Integer size) {
        log.debug("Sending GET request for bookings of owner ID: {} with state: {}", userId, state);
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}