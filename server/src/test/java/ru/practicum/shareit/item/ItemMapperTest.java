package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Test
    void shouldMapItemToItemDto() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("Owner");

        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequestId(10L);

        ItemDto itemDto = itemMapper.toItemDto(item);

        assertNotNull(itemDto);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(item.getRequestId(), itemDto.getRequestId());
    }

    @Test
    void shouldMapItemDtoToItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);
        itemDto.setRequestId(10L);

        User owner = new User();
        owner.setId(1L);

        Item item = itemMapper.toItem(itemDto, owner);

        assertNotNull(item);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getRequestId(), item.getRequestId());
        assertEquals(owner, item.getOwner());
    }

    @Test
    void shouldHandleNullValues() {
        ItemDto itemDto = itemMapper.toItemDto(null);
        assertNull(itemDto);

        Item item = itemMapper.toItem(null, null);
        assertNull(item);
    }
}