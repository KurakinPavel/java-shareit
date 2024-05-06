package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Booking {
    protected int id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected BookingStatus status;
    protected User booker;
    protected Item item;
}
