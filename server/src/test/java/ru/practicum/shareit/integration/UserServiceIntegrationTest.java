package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void create_shouldCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");

        UserDto result = userService.create(userDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@mail.com", result.getEmail());
    }

    @Test
    void create_shouldThrowExceptionForDuplicateEmail() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("John Doe");
        userDto1.setEmail("john@mail.com");
        userService.create(userDto1);

        UserDto userDto2 = new UserDto();
        userDto2.setName("Jane Doe");
        userDto2.setEmail("john@mail.com");

        assertThrows(ConflictException.class, () -> userService.create(userDto2));
    }

    @Test
    void update_shouldUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");
        UserDto createdUser = userService.create(userDto);

        UserDto updateDto = new UserDto();
        updateDto.setName("John Smith");
        updateDto.setEmail("john.smith@mail.com");

        UserDto result = userService.update(createdUser.getId(), updateDto);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@mail.com", result.getEmail());
    }

    @Test
    void update_shouldUpdateOnlyName() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");
        UserDto createdUser = userService.create(userDto);

        UserDto updateDto = new UserDto();
        updateDto.setName("John Smith");

        UserDto result = userService.update(createdUser.getId(), updateDto);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john@mail.com", result.getEmail());
    }

    @Test
    void update_shouldThrowExceptionForNonExistentUser() {
        UserDto updateDto = new UserDto();
        updateDto.setName("John Smith");

        Long nonExistentUserId = 999L;
        assertThrows(NotFoundException.class, () -> userService.update(nonExistentUserId, updateDto));
    }

    @Test
    void getById_shouldReturnUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");
        UserDto createdUser = userService.create(userDto);

        UserDto result = userService.getById(createdUser.getId());

        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@mail.com", result.getEmail());
    }

    @Test
    void getById_shouldThrowExceptionForNonExistentUser() {
        Long nonExistentUserId = 999L;
        assertThrows(NotFoundException.class, () -> userService.getById(nonExistentUserId));
    }

    @Test
    void getAll_shouldReturnAllUsers() {
        UserDto user1 = new UserDto();
        user1.setName("User 1");
        user1.setEmail("user1@mail.com");
        userService.create(user1);

        UserDto user2 = new UserDto();
        user2.setName("User 2");
        user2.setEmail("user2@mail.com");
        userService.create(user2);

        List<UserDto> result = userService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void getAll_shouldReturnEmptyList() {
        List<UserDto> result = userService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_shouldDeleteUser() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");
        UserDto createdUser = userService.create(userDto);

        userService.delete(createdUser.getId());

        Long userId = createdUser.getId();
        assertThrows(NotFoundException.class, () -> userService.getById(userId));
    }

    @Test
    void delete_shouldThrowExceptionForNonExistentUser() {
        Long nonExistentUserId = 999L;
        assertThrows(NotFoundException.class, () -> userService.delete(nonExistentUserId));
    }
}