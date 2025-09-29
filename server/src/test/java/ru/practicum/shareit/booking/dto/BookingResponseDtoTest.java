package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class BookingResponseDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserialize() throws JsonProcessingException {
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Мощная дрель", true, null);
        UserDto userDto = new UserDto(2L, "John Doe", "john@mail.com");

        BookingResponseDto originalDto = new BookingResponseDto(
                1L,
                LocalDateTime.of(2023, 12, 25, 10, 0),
                LocalDateTime.of(2023, 12, 26, 10, 0),
                BookingStatus.APPROVED,
                itemDto,
                userDto
        );

        String json = objectMapper.writeValueAsString(originalDto);
        BookingResponseDto deserializedDto = objectMapper.readValue(json, BookingResponseDto.class);

        assertEquals(originalDto.getId(), deserializedDto.getId());
        assertEquals(originalDto.getStart(), deserializedDto.getStart());
        assertEquals(originalDto.getEnd(), deserializedDto.getEnd());
        assertEquals(originalDto.getStatus(), deserializedDto.getStatus());
        assertNotNull(deserializedDto.getItem());
        assertNotNull(deserializedDto.getBooker());
    }

    @Test
    void shouldCreateBookingResponseDto() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Мощная дрель", true, null);
        UserDto userDto = new UserDto(2L, "John Doe", "john@mail.com");

        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(1L);
        dto.setStart(start);
        dto.setEnd(end);
        dto.setStatus(BookingStatus.WAITING);
        dto.setItem(itemDto);
        dto.setBooker(userDto);

        assertEquals(1L, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(BookingStatus.WAITING, dto.getStatus());
        assertEquals(itemDto, dto.getItem());
        assertEquals(userDto, dto.getBooker());
    }
}