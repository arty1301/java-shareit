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

    @Test
    void shouldHaveNoArgsConstructor() {
        ItemDto dto = new ItemDto();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getAvailable());
        assertNull(dto.getRequestId());
    }

    @Test
    void shouldHaveGettersAndSetters() {
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
    void shouldHaveEqualsAndHashCode() {
        ItemDto dto1 = new ItemDto(1L, "Дрель", "Описание", true, 10L);
        ItemDto dto2 = new ItemDto(1L, "Дрель", "Описание", true, 10L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldHaveToString() {
        ItemDto dto = new ItemDto(1L, "Дрель", "Мощная дрель", true, 10L);
        String toString = dto.toString();

        assertTrue(toString.contains("Дрель"));
        assertTrue(toString.contains("Мощная дрель"));
        assertTrue(toString.contains("true"));
    }

    @Test
    void shouldNotBeEqualWithDifferentValues() {
        ItemDto dto1 = new ItemDto(1L, "Дрель", "Описание", true, 10L);
        ItemDto dto2 = new ItemDto(2L, "Молоток", "Другое описание", false, 20L);

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}