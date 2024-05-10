package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemDtoWithBookingInformation {
    protected Integer id;
    protected String name;
    protected String description;
    protected Boolean available;
    protected Booking.BookingDtoForItemInformation lastBooking;
    protected Booking.BookingDtoForItemInformation nextBooking;
    protected List<CommentDtoOut> comments;
}
