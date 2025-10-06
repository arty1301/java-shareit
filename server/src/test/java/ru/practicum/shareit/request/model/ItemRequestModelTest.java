package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestModelTest {

    @Test
    void shouldCreateItemRequestWithBuilder() {
        User requester = new User();
        LocalDateTime created = LocalDateTime.now();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Нужна дрель")
                .requester(requester)
                .created(created)
                .build();

        assertEquals(1L, itemRequest.getId());
        assertEquals("Нужна дрель", itemRequest.getDescription());
        assertEquals(requester, itemRequest.getRequester());
        assertEquals(created, itemRequest.getCreated());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        ItemRequest request1 = new ItemRequest();
        request1.setId(1L);
        request1.setDescription("Description");

        ItemRequest request2 = new ItemRequest();
        request2.setId(1L);
        request2.setDescription("Description");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}