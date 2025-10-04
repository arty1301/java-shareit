package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemModelTest {

    @Test
    void shouldCreateItemWithBuilder() {
        User owner = new User();
        owner.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(10L);

        Item item = Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Мощная дрель")
                .available(true)
                .owner(owner)
                .request(itemRequest)
                .build();

        assertEquals(1L, item.getId());
        assertEquals("Дрель", item.getName());
        assertEquals("Мощная дрель", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(itemRequest, item.getRequest());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Дрель");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Дрель");

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }
}