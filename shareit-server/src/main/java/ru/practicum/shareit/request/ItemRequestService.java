package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestRequestDto itemRequestRequestDto);

    List<ItemRequestDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getOtherUsersRequests(Long userId, Integer from, Integer size);

    ItemRequestDto getById(Long userId, Long requestId);
}