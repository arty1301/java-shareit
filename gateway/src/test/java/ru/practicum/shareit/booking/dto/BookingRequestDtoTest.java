package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class BookingRequestDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserialize() throws JsonProcessingException {
        BookingRequestDto originalDto = new BookingRequestDto();
        originalDto.setItemId(1L);
        originalDto.setStart(LocalDateTime.of(2023, 12, 25, 10, 0));
        originalDto.setEnd(LocalDateTime.of(2023, 12, 26, 10, 0));

        String json = objectMapper.writeValueAsString(originalDto);
        BookingRequestDto deserializedDto = objectMapper.readValue(json, BookingRequestDto.class);

        assertEquals(originalDto.getItemId(), deserializedDto.getItemId());
        assertEquals(originalDto.getStart(), deserializedDto.getStart());
        assertEquals(originalDto.getEnd(), deserializedDto.getEnd());
    }

    @Test
    void shouldCreateBookingRequestDto() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        assertEquals(1L, dto.getItemId());
        assertNotNull(dto.getStart());
        assertNotNull(dto.getEnd());
        assertTrue(dto.getStart().isBefore(dto.getEnd()));
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingRequestDto dto = new BookingRequestDto(1L, start, end);

        assertEquals(1L, dto.getItemId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
    }

    @Test
    void shouldHaveNoArgsConstructor() {
        BookingRequestDto dto = new BookingRequestDto();

        assertNull(dto.getItemId());
        assertNull(dto.getStart());
        assertNull(dto.getEnd());
    }
}