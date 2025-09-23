package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemWithBookingsDto getById(Long userId, Long itemId);

    List<ItemWithBookingsDto> getAllByUser(Long userId);

    List<ItemDto> search(String text);

    CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}