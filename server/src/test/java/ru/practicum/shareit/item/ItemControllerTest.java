package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private ItemWithBookingsDto itemWithBookingsDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);

        itemWithBookingsDto = new ItemWithBookingsDto();
        itemWithBookingsDto.setId(1L);
        itemWithBookingsDto.setName("Дрель");
        itemWithBookingsDto.setDescription("Мощная дрель");
        itemWithBookingsDto.setAvailable(true);
        itemWithBookingsDto.setLastBooking(new BookingShortDto(1L, 2L,
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)));
        itemWithBookingsDto.setComments(Collections.emptyList());

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Отличная дрель!");
        commentDto.setAuthorName("User");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void create_shouldReturnItemDto() throws Exception {
        when(itemService.create(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Мощная дрель"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void update_shouldReturnUpdatedItem() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дрель"));
    }

    @Test
    void getById_shouldReturnItemWithBookings() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemWithBookingsDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lastBooking.id").value(1L))
                .andExpect(jsonPath("$.comments").isArray());
    }

    @Test
    void getAllByUser_shouldReturnItemsList() throws Exception {
        when(itemService.getAllByUser(anyLong())).thenReturn(List.of(itemWithBookingsDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    void search_shouldReturnItems() throws Exception {
        when(itemService.search(anyString())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "дрель"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    void search_shouldReturnEmptyListForBlankText() throws Exception {
        when(itemService.search(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addComment_shouldReturnComment() throws Exception {
        CommentRequestDto commentRequest = new CommentRequestDto();
        commentRequest.setText("Отличная дрель!");

        when(itemService.addComment(anyLong(), anyLong(), any(CommentRequestDto.class)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Отличная дрель!"))
                .andExpect(jsonPath("$.authorName").value("User"));
    }
}