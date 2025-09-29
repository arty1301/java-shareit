package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.user.UserClient;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @MockBean
    private ItemClient itemClient;

    @MockBean
    private UserClient userClient;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Test
    void shouldHandleAllEndpoints() throws Exception {
        when(bookingClient.create(anyLong(), any())).thenReturn(ResponseEntity.ok().build());
        when(bookingClient.updateStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(ResponseEntity.ok().build());
        when(bookingClient.getById(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());
        when(bookingClient.getAllByBooker(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(ResponseEntity.ok().build());
        when(bookingClient.getAllByOwner(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(ResponseEntity.ok().build());

        when(itemClient.create(anyLong(), any())).thenReturn(ResponseEntity.ok().build());
        when(itemClient.update(anyLong(), anyLong(), any())).thenReturn(ResponseEntity.ok().build());
        when(itemClient.getById(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());
        when(itemClient.getAllByUser(anyLong())).thenReturn(ResponseEntity.ok().build());
        when(itemClient.search(anyString(), anyLong())).thenReturn(ResponseEntity.ok().build());
        when(itemClient.addComment(anyLong(), anyLong(), any())).thenReturn(ResponseEntity.ok().build());

        when(userClient.create(any())).thenReturn(ResponseEntity.ok().build());
        when(userClient.update(anyLong(), any())).thenReturn(ResponseEntity.ok().build());
        when(userClient.getById(anyLong())).thenReturn(ResponseEntity.ok().build());
        when(userClient.getAll()).thenReturn(ResponseEntity.ok().build());
        when(userClient.delete(anyLong())).thenReturn(ResponseEntity.ok().build());

        when(itemRequestClient.create(anyLong(), any())).thenReturn(ResponseEntity.ok().build());
        when(itemRequestClient.getOwnRequests(anyLong())).thenReturn(ResponseEntity.ok().build());
        when(itemRequestClient.getOtherUsersRequests(anyLong(), anyInt(), anyInt())).thenReturn(ResponseEntity.ok().build());
        when(itemRequestClient.getById(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        mockMvc.perform(get("/items/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/requests/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
}