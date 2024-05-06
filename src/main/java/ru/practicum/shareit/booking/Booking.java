package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class Booking {
    protected int id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected Item item;
    protected User booker;
    protected BookingStatus status;
}
