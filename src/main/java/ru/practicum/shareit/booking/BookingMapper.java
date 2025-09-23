package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "bookerId", source = "booker.id")
    BookingDto toBookingDto(Booking booking);

    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "booker.id", source = "bookerId")
    Booking toBooking(BookingDto bookingDto);

    @Mapping(target = "item.id", source = "itemId")
    Booking toBooking(BookingRequestDto bookingRequestDto);

    @Mapping(target = "item", source = "booking.item")
    @Mapping(target = "booker", source = "booking.booker")
    BookingResponseDto toBookingResponseDto(Booking booking);
}