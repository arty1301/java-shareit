package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) {
        User owner = getUserOrThrow(userId);
        Item item = itemMapper.toItem(itemDto, owner);
        Item savedItem = itemRepository.save(item);
        log.info("Created item with ID: {}", savedItem.getId());
        return itemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item existingItem = getItemOrThrow(itemId);
        checkOwnership(userId, existingItem);

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(existingItem);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemWithBookingsDto getById(Long userId, Long itemId) {
        Item item = getItemOrThrow(itemId);
        ItemWithBookingsDto itemDto = convertToItemWithBookingsDto(item);

        if (item.getOwner().getId().equals(userId)) {
            addBookingInfo(item, itemDto);
        }

        addCommentsInfo(item, itemDto);
        return itemDto;
    }

    @Override
    public List<ItemWithBookingsDto> getAllByUser(Long userId) {
        getUserOrThrow(userId);
        List<Item> items = itemRepository.findByOwnerIdOrderById(userId);
        Map<Long, ItemWithBookingsDto> itemDtos = new LinkedHashMap<>();

        for (Item item : items) {
            ItemWithBookingsDto itemDto = convertToItemWithBookingsDto(item);
            addBookingInfo(item, itemDto);
            itemDtos.put(item.getId(), itemDto);
        }

        addCommentsInfo(itemDtos);
        return new ArrayList<>(itemDtos.values());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.searchAvailableItems(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User author = getUserOrThrow(userId);
        Item item = getItemOrThrow(itemId);

        validateCommentCreation(userId, itemId);

        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Added comment with ID: {} to item ID: {}", savedComment.getId(), itemId);

        return commentMapper.toCommentDto(savedComment);
    }

    private ItemWithBookingsDto convertToItemWithBookingsDto(Item item) {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        return dto;
    }

    private void addBookingInfo(Item item, ItemWithBookingsDto itemDto) {
        LocalDateTime now = LocalDateTime.now();
        Pageable limit = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "start"));

        List<Booking> lastBookings = bookingRepository.findByItemIdAndEndIsBeforeOrderByStartDesc(
                item.getId(), now, limit);
        if (!lastBookings.isEmpty()) {
            itemDto.setLastBooking(convertToBookingShortDto(lastBookings.get(0)));
        }

        List<Booking> nextBookings = bookingRepository.findByItemIdAndStartIsAfterOrderByStartDesc(
                item.getId(), now, limit);
        if (!nextBookings.isEmpty()) {
            itemDto.setNextBooking(convertToBookingShortDto(nextBookings.get(0)));
        }
    }

    private BookingShortDto convertToBookingShortDto(Booking booking) {
        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    private void addCommentsInfo(Item item, ItemWithBookingsDto itemDto) {
        List<Comment> comments = commentRepository.findByItemId(item.getId());
        itemDto.setComments(comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));
    }

    private void addCommentsInfo(Map<Long, ItemWithBookingsDto> itemDtos) {
        List<Long> itemIds = new ArrayList<>(itemDtos.keySet());
        List<Comment> comments = commentRepository.findByItemIdIn(itemIds);

        Map<Long, List<CommentDto>> commentsByItem = comments.stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(commentMapper::toCommentDto, Collectors.toList())
                ));

        itemDtos.forEach((itemId, itemDto) -> {
            itemDto.setComments(commentsByItem.getOrDefault(itemId, Collections.emptyList()));
        });
    }

    private void validateCommentCreation(Long userId, Long itemId) {
        List<Booking> userBookings = bookingRepository.findByItemIdAndBookerIdAndEndIsBefore(
                itemId, userId, LocalDateTime.now());

        if (userBookings.isEmpty()) {
            throw new BadRequestException("User can only comment on items they have booked in the past");
        }
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with ID: " + itemId));
    }

    private void checkOwnership(Long userId, Item item) {
        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("You are not the owner of this item");
        }
    }
}