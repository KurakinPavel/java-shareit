package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDtoForBookingOut;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDtoForBookingOut;

public class BookingMapper {

    public static Booking toBooking(BookingDtoForIn bookingDtoForIn, User booker, Item item) {
        return new Booking(
                bookingDtoForIn.getStart(),
                bookingDtoForIn.getEnd(),
                BookingStatus.WAITING,
                booker, item
        );
    }

    public static BookingDtoForOut toBookingDtoForOut(Booking booking) {
        return new BookingDtoForOut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new UserDtoForBookingOut(booking.getBooker().getId()),
                new ItemDtoForBookingOut(booking.getItem().getId(), booking.getItem().getName())
        );
    }

    public static BookingDtoForItemInformation toBookingDtoForItemInformation(Booking booking) {
        return new BookingDtoForItemInformation(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}
