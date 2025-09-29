package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;


@Slf4j
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit.server.url}") String serverUrl, RestTemplate rest) {
        super(rest, serverUrl);
        log.info("UserClient initialized with server URL: {}", serverUrl);
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        log.debug("Sending POST request to create user: {}", userDto.getEmail());
        return post(API_PREFIX, null, userDto);
    }

    public ResponseEntity<Object> update(Long userId, UserDto userDto) {
        log.debug("Sending PATCH request to update user ID: {}", userId);
        return patch(API_PREFIX + "/" + userId, null, userDto);
    }

    public ResponseEntity<Object> getById(Long userId) {
        log.debug("Sending GET request for user ID: {}", userId);
        return get(API_PREFIX + "/" + userId, null);
    }

    public ResponseEntity<Object> getAll() {
        log.debug("Sending GET request for all users");
        return get(API_PREFIX, null);
    }

    public ResponseEntity<Object> delete(Long userId) {
        log.debug("Sending DELETE request for user ID: {}", userId);
        return delete(API_PREFIX + "/" + userId, null);
    }
}