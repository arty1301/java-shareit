package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.shareit.item.dto.ItemDto;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto) {
        log.info("Received POST /items from user ID: {}", userId);
        ItemDto createdItem = itemService.create(userId, itemDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdItem.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto) {
        log.info("Received PATCH /items/{} from user ID: {}", itemId, userId);
        return ResponseEntity.ok(itemService.update(userId, itemId, itemDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("Received GET /items/{} from user ID: {}", itemId, userId);
        return ResponseEntity.ok(itemService.getById(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllByUser(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Received GET /items from user ID: {}", userId);
        return ResponseEntity.ok(itemService.getAllByUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(
            @RequestParam String text,
            @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        log.info("Received search request with text: '{}' from user ID: {}", text, userId);
        return ResponseEntity.ok(itemService.search(text));
    }
}