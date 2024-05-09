package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingDtoForItemInformation {
    protected Integer id;
    protected Integer bookerId;
    protected LocalDateTime start;
    protected LocalDateTime end;
}
