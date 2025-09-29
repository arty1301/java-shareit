package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentModelTest {

    @Test
    void shouldCreateCommentWithBuilder() {
        Item item = new Item();
        User author = new User();
        LocalDateTime created = LocalDateTime.now();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Отличный инструмент!")
                .item(item)
                .author(author)
                .created(created)
                .build();

        assertEquals(1L, comment.getId());
        assertEquals("Отличный инструмент!", comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(author, comment.getAuthor());
        assertEquals(created, comment.getCreated());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("Text");

        Comment comment2 = new Comment();
        comment2.setId(1L);
        comment2.setText("Text");

        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());
    }
}