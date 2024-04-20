package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {

    Item add(int ownerId, Item item);

    Item update(int ownerId, int itemId, ItemDto itemDto);

    Item getItem(int itemId);

    List<Item> getItemsOfOwner(int ownerId);

    List<Item> getItemsForRent(String text);

}
