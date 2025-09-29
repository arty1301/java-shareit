package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class UserDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSerializeAndDeserialize() throws JsonProcessingException {
        UserDto originalDto = new UserDto(1L, "John Doe", "john@mail.com");

        String json = objectMapper.writeValueAsString(originalDto);
        UserDto deserializedDto = objectMapper.readValue(json, UserDto.class);

        assertEquals(originalDto.getId(), deserializedDto.getId());
        assertEquals(originalDto.getName(), deserializedDto.getName());
        assertEquals(originalDto.getEmail(), deserializedDto.getEmail());
    }

    @Test
    void shouldCreateUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("John Doe");
        dto.setEmail("john@mail.com");

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals("john@mail.com", dto.getEmail());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        UserDto dto = new UserDto(1L, "John Doe", "john@mail.com");

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals("john@mail.com", dto.getEmail());
    }

    @Test
    void shouldHaveNoArgsConstructor() {
        UserDto dto = new UserDto();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
    }

    @Test
    void shouldHaveGettersAndSetters() {
        UserDto dto = new UserDto();

        dto.setId(1L);
        dto.setName("John Doe");
        dto.setEmail("john@mail.com");

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals("john@mail.com", dto.getEmail());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        UserDto dto1 = new UserDto(1L, "John Doe", "john@mail.com");
        UserDto dto2 = new UserDto(1L, "John Doe", "john@mail.com");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void shouldHaveToString() {
        UserDto dto = new UserDto(1L, "John Doe", "john@mail.com");
        String toString = dto.toString();

        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("john@mail.com"));
    }

    @Test
    void shouldNotBeEqualWithDifferentValues() {
        UserDto dto1 = new UserDto(1L, "John Doe", "john@mail.com");
        UserDto dto2 = new UserDto(2L, "Jane Doe", "jane@mail.com");

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}