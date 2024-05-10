package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoForIn;
import ru.practicum.shareit.booking.dto.BookingDtoForOut;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDtoForBookingOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDtoForBookingOut;

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

    public static Booking.BookingDtoForItemInformation toBookingDtoForItemInformation(Booking booking) {
        return new Booking.BookingDtoForItemInformation(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}
