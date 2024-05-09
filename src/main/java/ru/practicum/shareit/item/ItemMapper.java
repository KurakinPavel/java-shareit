package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingDtoForItemInformation;
import ru.practicum.shareit.user.User;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static ItemDtoWithBookingInformation toItemDtoWithBookingInformation(
            Item item, BookingDtoForItemInformation lastBooking, BookingDtoForItemInformation nextBooking) {
        return new ItemDtoWithBookingInformation(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking, nextBooking
        );
    }

    public static ItemDtoForBookingOut toItemDtoForBookingOut(Item item) {
        return new ItemDtoForBookingOut(
                item.getId(),
                item.getName()
        );
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId() != null ? itemDto.getId() : 0,
                itemDto.getName() != null ? itemDto.getName() : "",
                itemDto.getDescription() != null ? itemDto.getDescription() : "",
                itemDto.getAvailable() != null ? itemDto.getAvailable() : null,
                owner
        );
    }
}
