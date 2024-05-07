package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    @Min(1)
    protected Integer itemId;
    @Future
    protected LocalDateTime start;
    @Future
    protected LocalDateTime end;

    public BookingDto(Integer itemId, LocalDateTime start, LocalDateTime end)  {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
