package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items = new HashMap<>();
    private long idCounter = 1;
    private final UserService userService;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        log.info("Creating new item for user ID: {}", userId);

        validateItemFields(itemDto);
        User owner = getUserOrThrow(userId);

        Item item = ItemMapper.toItem(itemDto, owner);
        item.setId(idCounter++);
        items.put(item.getId(), item);

        log.info("Item created successfully with ID: {}", item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        log.info("Updating item ID: {} for user ID: {}", itemId, userId);

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

        log.info("Item with ID {} updated successfully", itemId);
        return ItemMapper.toItemDto(existingItem);
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        log.info("Getting item by ID: {} for user ID: {}", itemId, userId);
        return ItemMapper.toItemDto(getItemOrThrow(itemId));
    }

    @Override
    public List<ItemDto> getAllByUser(Long userId) {
        log.info("Getting all items for user ID: {}", userId);
        getUserOrThrow(userId); // Проверяем что пользователь существует

        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        log.info("Searching items with text: {}", text);

        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(searchText) ||
                                item.getDescription().toLowerCase().contains(searchText)))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User getUserOrThrow(Long userId) {
        try {
            return UserMapper.toUser(userService.getById(userId));
        } catch (IllegalArgumentException e) {
            log.error("User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }
    }

    private Item getItemOrThrow(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            log.error("Item not found with ID: {}", itemId);
            throw new IllegalArgumentException("Item not found with ID: " + itemId);
        }
        return item;
    }

    private void checkOwnership(Long userId, Item item) {
        if (!item.getOwner().getId().equals(userId)) {
            log.error("User ID {} is not the owner of item ID {}", userId, item.getId());
            throw new ForbiddenException("You are not the owner of this item");
        }
    }

    private void validateItemFields(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Item description cannot be empty");
        }
        if (itemDto.getAvailable() == null) {
            throw new IllegalArgumentException("Available status cannot be null");
        }
    }
}