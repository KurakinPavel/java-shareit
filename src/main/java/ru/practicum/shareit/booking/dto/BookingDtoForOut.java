package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoForBookingOut;
import ru.practicum.shareit.user.dto.UserDtoForBookingOut;

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
