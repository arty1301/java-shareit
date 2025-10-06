package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSerializeAndDeserialize() throws JsonProcessingException {
        ItemDto originalDto = new ItemDto(1L, "Дрель", "Мощная дрель", true, 10L);

        String json = objectMapper.writeValueAsString(originalDto);
        ItemDto deserializedDto = objectMapper.readValue(json, ItemDto.class);

        assertEquals(originalDto.getId(), deserializedDto.getId());
        assertEquals(originalDto.getName(), deserializedDto.getName());
        assertEquals(originalDto.getDescription(), deserializedDto.getDescription());
        assertEquals(originalDto.getAvailable(), deserializedDto.getAvailable());
        assertEquals(originalDto.getRequestId(), deserializedDto.getRequestId());
    }

    @Test
    void shouldCreateItemDto() {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Дрель");
        dto.setDescription("Мощная дрель");
        dto.setAvailable(true);
        dto.setRequestId(10L);

        assertEquals(1L, dto.getId());
        assertEquals("Дрель", dto.getName());
        assertEquals("Мощная дрель", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertEquals(10L, dto.getRequestId());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        ItemDto dto = new ItemDto(1L, "Дрель", "Мощная дрель", true, 10L);

        assertEquals(1L, dto.getId());
        assertEquals("Дрель", dto.getName());
        assertEquals("Мощная дрель", dto.getDescription());
        assertTrue(dto.getAvailable());
        assertEquals(10L, dto.getRequestId());
    }
}