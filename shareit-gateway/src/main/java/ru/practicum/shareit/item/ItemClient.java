package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Slf4j
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit.server.url}") String serverUrl, RestTemplate rest) {
        super(rest, serverUrl);
        log.info("ItemClient initialized with server URL: {}", serverUrl);
    }

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        log.debug("Sending POST request to create item for user ID: {}", userId);
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Sending PATCH request to update item ID: {} for user ID: {}", itemId, userId);
        return patch(API_PREFIX + "/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getById(Long userId, Long itemId) {
        log.debug("Sending GET request for item ID: {} for user ID: {}", itemId, userId);
        return get(API_PREFIX + "/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllByUser(Long userId) {
        log.debug("Sending GET request for all items of user ID: {}", userId);
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> search(String text, Long userId) {
        log.debug("Sending GET request to search items with text: '{}'", text);
        Map<String, Object> parameters = Map.of("text", text);
        return get(API_PREFIX + "/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        log.debug("Sending POST request to add comment to item ID: {} by user ID: {}", itemId, userId);
        return post(API_PREFIX + "/" + itemId + "/comment", userId, commentRequestDto);
    }
}