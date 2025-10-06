package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@mail.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);
    }

    @Test
    void create_shouldCreateBooking() {
        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto result = bookingService.create(booker.getId(), bookingRequest);

        assertNotNull(result);
        assertEquals(BookingStatus.WAITING, result.getStatus());
        assertEquals(item.getId(), result.getItem().getId());
        assertEquals(booker.getId(), result.getBooker().getId());
    }

    @Test
    void create_shouldThrowExceptionWhenBookingOwnItem() {
        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        Long ownerId = owner.getId();
        assertThrows(ForbiddenException.class, () ->
                bookingService.create(ownerId, bookingRequest));
    }

    @Test
    void create_shouldThrowExceptionWhenItemNotAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        Long bookerId = booker.getId();
        assertThrows(BadRequestException.class, () ->
                bookingService.create(bookerId, bookingRequest));
    }

    @Test
    void updateStatus_shouldApproveBooking() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);

        BookingResponseDto result = bookingService.updateStatus(owner.getId(), savedBooking.getId(), true);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void updateStatus_shouldRejectBooking() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);

        BookingResponseDto result = bookingService.updateStatus(owner.getId(), savedBooking.getId(), false);

        assertNotNull(result);
        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void updateStatus_shouldThrowExceptionWhenNotOwner() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);

        Long bookerId = booker.getId();
        Long bookingId = savedBooking.getId();
        assertThrows(ForbiddenException.class, () ->
                bookingService.updateStatus(bookerId, bookingId, true));
    }

    @Test
    void getById_shouldReturnBookingForBooker() {
        Booking booking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        BookingResponseDto result = bookingService.getById(booker.getId(), booking.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getById_shouldReturnBookingForOwner() {
        Booking booking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        BookingResponseDto result = bookingService.getById(owner.getId(), booking.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getById_shouldThrowExceptionForUnauthorizedUser() {
        User otherUser = new User();
        otherUser.setName("Other");
        otherUser.setEmail("other@mail.com");
        User savedOtherUser = userRepository.save(otherUser);

        Booking booking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        Long otherUserId = savedOtherUser.getId();
        Long bookingId = booking.getId();
        assertThrows(ForbiddenException.class, () ->
                bookingService.getById(otherUserId, bookingId));
    }

    @Test
    void getAllByBooker_shouldReturnAllBookings() {
        Booking booking1 = createBooking(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        Booking booking2 = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        List<BookingResponseDto> result = bookingService.getAllByBooker(booker.getId(), "ALL", 0, 10);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getStart().isAfter(result.get(1).getStart()));
    }

    @Test
    void getAllByBooker_shouldReturnCurrentBookings() {
        createBooking(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));

        List<BookingResponseDto> result = bookingService.getAllByBooker(booker.getId(), "CURRENT", 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllByBooker_shouldReturnPastBookings() {
        createBooking(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));

        List<BookingResponseDto> result = bookingService.getAllByBooker(booker.getId(), "PAST", 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllByBooker_shouldReturnFutureBookings() {
        createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        List<BookingResponseDto> result = bookingService.getAllByBooker(booker.getId(), "FUTURE", 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    void getAllByOwner_shouldReturnOwnerBookings() {
        Booking booking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        List<BookingResponseDto> result = bookingService.getAllByOwner(owner.getId(), "ALL", 0, 10);

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    private Booking createBooking(LocalDateTime start, LocalDateTime end) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        return bookingRepository.save(booking);
    }
}