package ru.practicum.shareit.request;

import ru.practicum.shareit.item.model.ItemDtoForItemRequestOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequestDtoOutWithItemsInformation;
import ru.practicum.shareit.user.model.User;

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
