package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingShortDtoTest {

    @Test
    void shouldCreateBookingShortDto() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingShortDto dto = new BookingShortDto();
        dto.setId(1L);
        dto.setBookerId(2L);
        dto.setStart(start);
        dto.setEnd(end);

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getBookerId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingShortDto dto = new BookingShortDto(1L, 2L, start, end);

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getBookerId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingShortDto dto1 = new BookingShortDto(1L, 2L, start, end);
        BookingShortDto dto2 = new BookingShortDto(1L, 2L, start, end);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}