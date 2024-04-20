package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto add(int ownerId, ItemDto itemDto) {
        userStorage.getUser(ownerId);
        if ((itemDto.getName() == null) || (itemDto.getName().isBlank()) || itemDto.getDescription() == null
                || itemDto.getDescription().isBlank() || itemDto.getAvailable() == null)
            throw new NullPointerException("Переданы некорректные данные для создания item");
        return ItemMapper.toItemDto(itemStorage.add(ownerId, ItemMapper.toItem(itemDto)));
    }

    public ItemDto update(int ownerId, int itemId, ItemDto itemDto) {
        userStorage.getUser(ownerId);
        return ItemMapper.toItemDto(itemStorage.update(ownerId, itemId, itemDto));
    }

    public ItemDto getItem(int itemId) {
        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    public List<ItemDto> getItemsOfOwner(int ownerId) {
        return itemStorage.getItemsOfOwner(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getItemsForRent(String text) {
        return itemStorage.getItemsForRent(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
