package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDtoForIn {
    protected Integer itemId;
    protected LocalDateTime start;
    protected LocalDateTime end;

    public BookingDtoForIn(Integer itemId, LocalDateTime start, LocalDateTime end)  {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
