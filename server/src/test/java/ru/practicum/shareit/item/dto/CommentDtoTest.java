package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class CommentDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserialize() throws JsonProcessingException {
        CommentDto originalDto = new CommentDto(1L, "Отличный инструмент!", "John Doe",
                LocalDateTime.of(2023, 12, 25, 10, 0));

        String json = objectMapper.writeValueAsString(originalDto);
        CommentDto deserializedDto = objectMapper.readValue(json, CommentDto.class);

        assertEquals(originalDto.getId(), deserializedDto.getId());
        assertEquals(originalDto.getText(), deserializedDto.getText());
        assertEquals(originalDto.getAuthorName(), deserializedDto.getAuthorName());
        assertEquals(originalDto.getCreated(), deserializedDto.getCreated());
    }

    @Test
    void shouldCreateCommentDto() {
        LocalDateTime created = LocalDateTime.now();
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Отличный инструмент!");
        dto.setAuthorName("John Doe");
        dto.setCreated(created);

        assertEquals(1L, dto.getId());
        assertEquals("Отличный инструмент!", dto.getText());
        assertEquals("John Doe", dto.getAuthorName());
        assertEquals(created, dto.getCreated());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        LocalDateTime created = LocalDateTime.now();
        CommentDto dto = new CommentDto(1L, "Отличный инструмент!", "John Doe", created);

        assertEquals(1L, dto.getId());
        assertEquals("Отличный инструмент!", dto.getText());
        assertEquals("John Doe", dto.getAuthorName());
        assertEquals(created, dto.getCreated());
    }
}