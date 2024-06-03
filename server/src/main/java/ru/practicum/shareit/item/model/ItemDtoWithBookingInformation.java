package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingDtoForItemInformation;

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
