package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 1;

    @Override
    public UserDto create(UserDto userDto) {
        log.info("Creating new user: {}", userDto);

        validateUserFields(userDto);
        checkEmailUniqueness(userDto.getEmail());

        User user = UserMapper.toUser(userDto);
        user.setId(idCounter++);
        users.put(user.getId(), user);

        log.info("User created successfully with ID: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        log.info("Updating user with ID: {}", userId);

        User existingUser = users.get(userId);
        if (existingUser == null) {
            log.error("User not found with ID: {}", userId);
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail())) {
            checkEmailUniqueness(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        log.info("User with ID {} updated successfully", userId);
        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getById(Long userId) {
        log.info("Getting user by ID: {}", userId);

        User user = users.get(userId);
        if (user == null) {
            log.error("User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Getting all users, count: {}", users.size());
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        if (users.remove(userId) == null) {
            log.error("User not found for deletion with ID: {}", userId);
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        log.info("User with ID {} deleted successfully", userId);
    }

    private void validateUserFields(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        if (!userDto.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email should contain '@'");
        }
    }

    private void checkEmailUniqueness(String email) {
        boolean emailExists = users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
        if (emailExists) {
            log.error("Email already exists: {}", email);
            throw new ConflictException("Email already exists: " + email);
        }
    }
}