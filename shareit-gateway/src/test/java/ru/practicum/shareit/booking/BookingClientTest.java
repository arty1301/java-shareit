package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookingClient bookingClient;

    private final String serverUrl = "http://localhost:9090";
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        bookingClient = new BookingClient(serverUrl, restTemplate);

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void create_shouldCallPostMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        bookingClient.create(1L, bookingRequestDto);

        verify(restTemplate).exchange(
                eq(serverUrl + "/bookings"),
                eq(org.springframework.http.HttpMethod.POST),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void updateStatus_shouldCallPatchMethodWithParameters() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok().build());

        bookingClient.updateStatus(1L, 1L, true);

        verify(restTemplate).exchange(
                eq(serverUrl + "/bookings/1?approved={approved}"),
                eq(org.springframework.http.HttpMethod.PATCH),
                any(),
                eq(Object.class),
                anyMap()
        );
    }

    @Test
    void getById_shouldCallGetMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        bookingClient.getById(1L, 1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/bookings/1"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void getAllByBooker_shouldCallGetMethodWithParameters() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok().build());

        bookingClient.getAllByBooker(1L, "ALL", 0, 10);

        verify(restTemplate).exchange(
                eq(serverUrl + "/bookings?state={state}&from={from}&size={size}"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class),
                anyMap()
        );
    }

    @Test
    void getAllByOwner_shouldCallGetMethodWithParameters() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok().build());

        bookingClient.getAllByOwner(1L, "ALL", 0, 10);

        verify(restTemplate).exchange(
                eq(serverUrl + "/bookings/owner?state={state}&from={from}&size={size}"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class),
                anyMap()
        );
    }
}