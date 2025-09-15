package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.User;

import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Available status cannot be null")
    private Boolean available;

    private User owner;
    private Long requestId;
}