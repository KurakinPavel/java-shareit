package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.model.BookingDtoForItemInformation;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getItemRequest() != null ? item.getItemRequest().getId() : 0
        );
    }

    public static ItemDtoWithBookingInformation toItemDtoWithBookingInformation(
            Item item, BookingDtoForItemInformation lastBooking, BookingDtoForItemInformation nextBooking,
            List<CommentDtoOut> comments) {
        return new ItemDtoWithBookingInformation(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking, nextBooking, comments
        );
    }

    public static ItemDtoForItemRequestOut toItemDtoForItemRequestOut(Item item) {
        return new ItemDtoForItemRequestOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getItemRequest().getId(),
                item.getAvailable()
        );
    }

    public static Item toItem(ItemDto itemDto, User owner, ItemRequest itemRequest) {
        return new Item(
                itemDto.getId() != null ? itemDto.getId() : 0,
                itemDto.getName() != null ? itemDto.getName() : "",
                itemDto.getDescription() != null ? itemDto.getDescription() : "",
                itemDto.getAvailable() != null ? itemDto.getAvailable() : null,
                owner, itemRequest
        );
    }
}
