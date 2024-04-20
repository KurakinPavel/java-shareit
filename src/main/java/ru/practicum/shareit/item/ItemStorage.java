package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {

    Item add(int ownerId, ItemDto itemDto);

    Item update(int ownerId, int itemId, ItemDto itemDto);

    ItemDto getItemDto(int itemId);

    List<Item> getItemsOfOwner(int ownerId);

    List<ItemDto> getItemsForRent(String text);

}
