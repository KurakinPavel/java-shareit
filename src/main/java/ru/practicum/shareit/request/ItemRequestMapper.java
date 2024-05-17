package ru.practicum.shareit.request;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDtoIn itemRequestDtoIn, User requester) {
        return new ItemRequest(
                itemRequestDtoIn.getId() != null ? itemRequestDtoIn.getId() : 0,
                itemRequestDtoIn.getDescription(), requester, LocalDateTime.now()
        );
    }

    public static ItemRequestDtoOut toItemRequestDtoOut(ItemRequest itemRequest) {
        return new ItemRequestDtoOut(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }
}
