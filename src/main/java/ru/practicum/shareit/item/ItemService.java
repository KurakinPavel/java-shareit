package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;

    public Item add(int ownerId, ItemDto itemDto) {
        return itemStorage.add(ownerId, itemDto);
    }

    public Item update(int ownerId, int itemId, ItemDto itemDto) {
        return itemStorage.update(ownerId, itemId, itemDto);
    }

    public ItemDto getItemDto(int itemId) {
        return itemStorage.getItemDto(itemId);
    }

    public List<Item> getItemsOfOwner(int ownerId) {
        return itemStorage.getItemsOfOwner(ownerId);
    }

    public List<ItemDto> getItemsForRent(String text) {
        return itemStorage.getItemsForRent(text);
    }

}
