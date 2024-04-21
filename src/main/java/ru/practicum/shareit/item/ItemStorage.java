package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {

    Item add(Item item);

    void update(Item item);

    Item getItem(int itemId);

    List<Item> getItemsOfOwner(int ownerId);

    List<Item> getItemsForRent(String text);

}
