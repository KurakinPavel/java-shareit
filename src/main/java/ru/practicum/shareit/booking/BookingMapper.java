package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId(),
                booking.getItem().getId(),
                booking.getItem().getName()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        return new Booking(
                bookingDto.getId() != null ? bookingDto.getId() : 0,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getStatus() != null ? bookingDto.getStatus() : BookingStatus.WAITING,
                booker, item
        );
    }
}
