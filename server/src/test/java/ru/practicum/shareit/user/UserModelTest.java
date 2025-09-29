package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void shouldCreateUserWithBuilder() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@mail.com")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@mail.com", user.getEmail());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setEmail("john@mail.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("John");
        user2.setEmail("john@mail.com");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldNotBeEqualWithDifferentIds() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        assertNotEquals(user1, user2);
    }
}