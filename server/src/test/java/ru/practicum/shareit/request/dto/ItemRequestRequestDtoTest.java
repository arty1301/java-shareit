package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestRequestDtoTest {

    @Test
    void shouldCreateItemRequestRequestDto() {
        ItemRequestRequestDto dto = new ItemRequestRequestDto();
        dto.setDescription("Нужна дрель");

        assertEquals("Нужна дрель", dto.getDescription());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        ItemRequestRequestDto dto = new ItemRequestRequestDto("Нужна дрель");

        assertEquals("Нужна дрель", dto.getDescription());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        ItemRequestRequestDto dto1 = new ItemRequestRequestDto("Описание");
        ItemRequestRequestDto dto2 = new ItemRequestRequestDto("Описание");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}