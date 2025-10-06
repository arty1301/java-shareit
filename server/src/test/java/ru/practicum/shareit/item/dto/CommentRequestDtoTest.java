package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentRequestDtoTest {

    @Test
    void shouldCreateCommentRequestDto() {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("Отличный инструмент!");

        assertEquals("Отличный инструмент!", dto.getText());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        CommentRequestDto dto = new CommentRequestDto("Отличный инструмент!");

        assertEquals("Отличный инструмент!", dto.getText());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        CommentRequestDto dto1 = new CommentRequestDto("Текст");
        CommentRequestDto dto2 = new CommentRequestDto("Текст");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}