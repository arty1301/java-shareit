package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import java.util.Map;

@Slf4j
@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit.server.url}") String serverUrl, RestTemplate rest) {
        super(rest, serverUrl);
        log.info("ItemRequestClient initialized with server URL: {}", serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, ItemRequestRequestDto itemRequestRequestDto) {
        log.debug("Sending POST request to create item request for user ID: {}", userId);
        return post(API_PREFIX, userId, itemRequestRequestDto);
    }

    public ResponseEntity<Object> getOwnRequests(Long userId) {
        log.debug("Sending GET request for own item requests of user ID: {}", userId);
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> getOtherUsersRequests(Long userId, Integer from, Integer size) {
        log.debug("Sending GET request for other users' item requests for user ID: {}", userId);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(Long userId, Long requestId) {
        log.debug("Sending GET request for item request ID: {} for user ID: {}", requestId, userId);
        return get(API_PREFIX + "/" + requestId, userId);
    }
}