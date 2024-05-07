package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        return new Booking(
                bookingDto.getStart(),
                bookingDto.getEnd(),
                BookingStatus.WAITING,
                booker, item
        );
    }
}
