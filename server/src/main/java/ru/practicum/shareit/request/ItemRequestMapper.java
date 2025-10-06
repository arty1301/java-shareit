package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "items", ignore = true)
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "requester", ignore = true)
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);
}