package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDtoForItemRequestOut;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDtoIn itemRequestDtoIn, User itemRequester) {
        return new ItemRequest(
                itemRequestDtoIn.getId() != null ? itemRequestDtoIn.getId() : 0,
                itemRequestDtoIn.getDescription(), itemRequester, LocalDateTime.now()
        );
    }

    public static ItemRequestDtoOut toItemRequestDtoOut(ItemRequest itemRequest) {
        return new ItemRequestDtoOut(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequestDtoOutWithItemsInformation toItemRequestDtoOutWithItemsInformation(ItemRequest itemRequest,
                                                                                                List<ItemDtoForItemRequestOut> items) {
        return new ItemRequestDtoOutWithItemsInformation(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }
}
