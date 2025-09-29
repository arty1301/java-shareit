package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User requester;
    private User owner;

    @BeforeEach
    void setUp() {
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        requester = new User();
        requester.setName("Requester");
        requester.setEmail("requester@mail.com");
        requester = userRepository.save(requester);

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        owner = userRepository.save(owner);
    }

    @Test
    void create_shouldCreateItemRequest() {
        ItemRequestRequestDto requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна мощная дрель");

        ItemRequestDto result = itemRequestService.create(requester.getId(), requestDto);

        assertNotNull(result);
        assertEquals("Нужна мощная дрель", result.getDescription());
        assertNotNull(result.getCreated());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void create_shouldThrowExceptionForNonExistentUser() {
        ItemRequestRequestDto requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна дрель");

        Long nonExistentUserId = 999L;
        assertThrows(NotFoundException.class, () ->
                itemRequestService.create(nonExistentUserId, requestDto));
    }

    @Test
    void getOwnRequests_shouldReturnUserRequests() {
        ItemRequestRequestDto requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна дрель");
        itemRequestService.create(requester.getId(), requestDto);

        List<ItemRequestDto> result = itemRequestService.getOwnRequests(requester.getId());

        assertEquals(1, result.size());
        assertEquals("Нужна дрель", result.get(0).getDescription());
    }

    @Test
    void getOwnRequests_shouldReturnEmptyList() {
        List<ItemRequestDto> result = itemRequestService.getOwnRequests(requester.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void getOtherUsersRequests_shouldReturnOtherUsersRequests() {
        ItemRequestRequestDto requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна дрель");
        itemRequestService.create(requester.getId(), requestDto);

        List<ItemRequestDto> result = itemRequestService.getOtherUsersRequests(owner.getId(), 0, 10);

        assertEquals(1, result.size());
        assertEquals("Нужна дрель", result.get(0).getDescription());
    }

    @Test
    void getOtherUsersRequests_shouldNotReturnOwnRequests() {
        ItemRequestRequestDto requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна дрель");
        itemRequestService.create(requester.getId(), requestDto);

        List<ItemRequestDto> result = itemRequestService.getOtherUsersRequests(requester.getId(), 0, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void getById_shouldReturnRequest() {
        ItemRequestRequestDto requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна дрель");
        ItemRequestDto createdRequest = itemRequestService.create(requester.getId(), requestDto);

        ItemRequestDto result = itemRequestService.getById(requester.getId(), createdRequest.getId());

        assertNotNull(result);
        assertEquals(createdRequest.getId(), result.getId());
        assertEquals("Нужна дрель", result.getDescription());
    }

    @Test
    void getById_shouldReturnRequestWithItems() {
        ItemRequestRequestDto requestDto = new ItemRequestRequestDto();
        requestDto.setDescription("Нужна дрель");
        ItemRequestDto createdRequest = itemRequestService.create(requester.getId(), requestDto);

        Item item = new Item();
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequestId(createdRequest.getId());
        itemRepository.save(item);

        ItemRequestDto result = itemRequestService.getById(requester.getId(), createdRequest.getId());

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Дрель", result.getItems().get(0).getName());
    }

    @Test
    void getById_shouldThrowExceptionForNonExistentRequest() {
        Long nonExistentRequestId = 999L;
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getById(requester.getId(), nonExistentRequestId));
    }
}