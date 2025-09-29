package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ItemClient itemClient;

    private final String serverUrl = "http://localhost:9090";
    private ItemDto itemDto;
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void setUp() {
        itemClient = new ItemClient(serverUrl, restTemplate);

        itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);

        commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Отличный инструмент!");
    }

    @Test
    void create_shouldCallPostMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        itemClient.create(1L, itemDto);

        verify(restTemplate).exchange(
                eq(serverUrl + "/items"),
                eq(org.springframework.http.HttpMethod.POST),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void update_shouldCallPatchMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        itemClient.update(1L, 1L, itemDto);

        verify(restTemplate).exchange(
                eq(serverUrl + "/items/1"),
                eq(org.springframework.http.HttpMethod.PATCH),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void getById_shouldCallGetMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        itemClient.getById(1L, 1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/items/1"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void getAllByUser_shouldCallGetMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        itemClient.getAllByUser(1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/items"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void search_shouldCallGetMethodWithParameters() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok().build());

        itemClient.search("дрель", 1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/items/search?text={text}"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class),
                anyMap()
        );
    }

    @Test
    void addComment_shouldCallPostMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        itemClient.addComment(1L, 1L, commentRequestDto);

        verify(restTemplate).exchange(
                eq(serverUrl + "/items/1/comment"),
                eq(org.springframework.http.HttpMethod.POST),
                any(),
                eq(Object.class)
        );
    }
}