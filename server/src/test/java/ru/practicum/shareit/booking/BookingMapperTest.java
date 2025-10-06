package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Test
    void shouldMapBookingToBookingDto() {
        Item item = new Item();
        item.setId(1L);

        User booker = new User();
        booker.setId(2L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        assertNotNull(bookingDto);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItemId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBookerId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void shouldMapBookingRequestDtoToBooking() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = bookingMapper.toBooking(requestDto);

        assertNotNull(booking);
        assertEquals(requestDto.getItemId(), booking.getItem().getId());
        assertEquals(requestDto.getStart(), booking.getStart());
        assertEquals(requestDto.getEnd(), booking.getEnd());
    }

    @Test
    void shouldHandleNullValues() {
        BookingDto bookingDto = bookingMapper.toBookingDto(null);
        assertNull(bookingDto);

        BookingResponseDto responseDto = bookingMapper.toBookingResponseDto(null);
        assertNull(responseDto);

        Booking booking = bookingMapper.toBooking((BookingRequestDto) null);
        assertNull(booking);
    }
}