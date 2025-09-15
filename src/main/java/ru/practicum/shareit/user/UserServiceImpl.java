package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 1;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        checkEmailUniqueness(userDto.getEmail());
        User user = userMapper.toUser(userDto);
        user.setId(idCounter++);
        users.put(user.getId(), user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User existingUser = users.get(userId);
        if (existingUser == null) {
            throw new NotFoundException("User not found with ID: " + userId);
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

        return userMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return users.values().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        if (users.remove(userId) == null) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
    }

    private void checkEmailUniqueness(String email) {
        boolean emailExists = users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
        if (emailExists) {
            throw new ConflictException("Email already exists: " + email);
        }
    }
}