package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
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
    void create_shouldCreateNewItem() {
        ItemDto newItemDto = new ItemDto();
        newItemDto.setName("Молоток");
        newItemDto.setDescription("Простой молоток");
        newItemDto.setAvailable(true);

        ItemDto result = itemService.create(owner.getId(), newItemDto);

        assertNotNull(result);
        assertEquals("Молоток", result.getName());
        assertEquals("Простой молоток", result.getDescription());
        assertTrue(result.getAvailable());
    }

    @Test
    void update_shouldUpdateExistingItem() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Обновленная дрель");
        updateDto.setDescription("Новое описание");
        updateDto.setAvailable(false);

        ItemDto result = itemService.update(owner.getId(), item.getId(), updateDto);

        assertNotNull(result);
        assertEquals("Обновленная дрель", result.getName());
        assertEquals("Новое описание", result.getDescription());
        assertFalse(result.getAvailable());
    }

    @Test
    void update_shouldThrowExceptionWhenNotOwner() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Новое имя");

        Long bookerId = booker.getId();
        Long itemId = item.getId();
        assertThrows(ForbiddenException.class, () ->
                itemService.update(bookerId, itemId, updateDto));
    }

    @Test
    void getById_shouldReturnItemForOwner() {
        ItemWithBookingsDto result = itemService.getById(owner.getId(), item.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals("Дрель", result.getName());
    }

    @Test
    void getById_shouldReturnItemForOtherUser() {
        ItemWithBookingsDto result = itemService.getById(booker.getId(), item.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals("Дрель", result.getName());
    }

    @Test
    void getAllByUser_shouldReturnItemsWithBookingsAndComments() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Comment comment = new Comment();
        comment.setText("Отличная дрель!");
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        List<ItemWithBookingsDto> result = itemService.getAllByUser(owner.getId());

        assertNotNull(result);
        assertEquals(1, result.size());

        ItemWithBookingsDto itemDto = result.get(0);
        assertEquals(item.getId(), itemDto.getId());
        assertNotNull(itemDto.getLastBooking());
        assertEquals(1, itemDto.getComments().size());
        assertEquals("Отличная дрель!", itemDto.getComments().get(0).getText());
    }

    @Test
    void search_shouldReturnAvailableItems() {
        List<ItemDto> result = itemService.search("дрель");

        assertEquals(1, result.size());
        assertEquals("Дрель", result.get(0).getName());
    }

    @Test
    void search_shouldReturnEmptyListForBlankText() {
        List<ItemDto> result = itemService.search("");

        assertTrue(result.isEmpty());
    }

    @Test
    void addComment_shouldAddCommentWhenUserHasPastBooking() {
        Booking pastBooking = new Booking();
        pastBooking.setStart(LocalDateTime.now().minusDays(3));
        pastBooking.setEnd(LocalDateTime.now().minusDays(1));
        pastBooking.setItem(item);
        pastBooking.setBooker(booker);
        pastBooking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(pastBooking);

        CommentRequestDto commentRequest = new CommentRequestDto();
        commentRequest.setText("Отличный инструмент!");

        var result = itemService.addComment(booker.getId(), item.getId(), commentRequest);

        assertNotNull(result);
        assertEquals("Отличный инструмент!", result.getText());
        assertEquals("Booker", result.getAuthorName());

        List<Comment> comments = commentRepository.findByItemId(item.getId());
        assertEquals(1, comments.size());
    }

    @Test
    void addComment_shouldThrowExceptionWhenNoPastBooking() {
        CommentRequestDto commentRequest = new CommentRequestDto();
        commentRequest.setText("Отличный инструмент!");

        Long bookerId = booker.getId();
        Long itemId = item.getId();
        assertThrows(BadRequestException.class, () ->
                itemService.addComment(bookerId, itemId, commentRequest));
    }
}