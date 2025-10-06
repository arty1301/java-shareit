package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserClient userClient;

    private final String serverUrl = "http://localhost:9090";
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userClient = new UserClient(serverUrl, restTemplate);

        userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");
    }

    @Test
    void create_shouldCallPostMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        userClient.create(userDto);

        verify(restTemplate).exchange(
                eq(serverUrl + "/users"),
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

        userClient.update(1L, userDto);

        verify(restTemplate).exchange(
                eq(serverUrl + "/users/1"),
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

        userClient.getById(1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/users/1"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void getAll_shouldCallGetMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        userClient.getAll();

        verify(restTemplate).exchange(
                eq(serverUrl + "/users"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Object.class)
        );
    }

    @Test
    void delete_shouldCallDeleteMethod() {
        when(restTemplate.exchange(
                anyString(),
                any(org.springframework.http.HttpMethod.class),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        userClient.delete(1L);

        verify(restTemplate).exchange(
                eq(serverUrl + "/users/1"),
                eq(org.springframework.http.HttpMethod.DELETE),
                any(),
                eq(Object.class)
        );
    }
}