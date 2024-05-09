package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingDtoForItemInformation;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemDtoWithBookingInformation {
    protected Integer id;
    protected String name;
    protected String description;
    protected Boolean available;
    protected BookingDtoForItemInformation lastBooking;
    protected BookingDtoForItemInformation nextBooking;
    protected List<CommentDtoOut> comments;
}
