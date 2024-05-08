package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.ItemDtoForBookingOut;
import ru.practicum.shareit.user.UserDtoForBookingOut;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingDtoForOut {
    protected Integer id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected BookingStatus status;
    protected UserDtoForBookingOut booker;
    protected ItemDtoForBookingOut item;
}
