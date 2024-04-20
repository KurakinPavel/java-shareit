package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Booking {
    protected int id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected int item;
    protected int booker;
    protected BookingStatus status;
}
