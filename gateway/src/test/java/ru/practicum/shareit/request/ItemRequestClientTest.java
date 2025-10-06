package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ItemRequestClient itemRequestClient;

    private final String serverUrl = "http://localhost:9090";
    private ItemRequestRequestDto itemRequestRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestClient = new ItemRequestClient(serverUrl, restTemplate);

        itemRequestRequestDto = new ItemRequestRequestDto();
        itemRequestRequestDto.setDescription("Нужна мощная дрель");
    }

    @Test
    void create_shouldCallPostMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        itemRequestClient.create(1L, itemRequestRequestDto);

        verify(restTemplate).exchange(
                eq(serverUrl + "/requests"),
                eq(org.springframework.http.HttpMethod.POST),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void getOwnRequests_shouldCallGetMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        itemRequestClient.getOwnRequests(1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/requests"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void getOtherUsersRequests_shouldCallGetMethodWithParameters() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok().build());

        itemRequestClient.getOtherUsersRequests(1L, 0, 10);

        verify(restTemplate).exchange(
                eq(serverUrl + "/requests/all?from={from}&size={size}"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class),
                anyMap()
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

        itemRequestClient.getById(1L, 1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/requests/1"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class)
        );
    }
}