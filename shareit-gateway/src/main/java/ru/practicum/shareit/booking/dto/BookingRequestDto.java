package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    @NotNull(message = "Item ID cannot be null")
    private Long itemId;

    @NotNull(message = "Start date cannot be null")
    @Future(message = "Start date must be in the future")
    private LocalDateTime start;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDateTime end;
}