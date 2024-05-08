package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDtoForIn {
    @Min(1)
    protected Integer itemId;
    @Future
    protected LocalDateTime start;
    @Future
    protected LocalDateTime end;

    public BookingDtoForIn(Integer itemId, LocalDateTime start, LocalDateTime end)  {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
