package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto add(int ownerId, ItemDto itemDto) {
        User owner = userStorage.getUserById(ownerId);
        if ((itemDto.getName() == null) || (itemDto.getName().isBlank()) || itemDto.getDescription() == null
                || itemDto.getDescription().isBlank() || itemDto.getAvailable() == null)
            throw new ItemValidationException("Переданы некорректные данные для создания item");
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    public ItemDto update(int ownerId, int itemId, ItemDto itemDto) {
        Item updatingItem = itemStorage.getItem(itemId);
        if (updatingItem.getOwner().getId() != ownerId)
            throw new NoSuchElementException("Редактировать данные item может только владелец");
        if (!(itemDto.getName() == null || itemDto.getName().isBlank())) {
            updatingItem.setName(itemDto.getName());
            log.info("Обновлено поле name item с id {}", itemId);
        }
        if (!(itemDto.getDescription() == null || itemDto.getDescription().isBlank())) {
            updatingItem.setDescription(itemDto.getDescription());
            log.info("Обновлено поле description item с id {}", itemId);
        }
        if (!(itemDto.getAvailable() == null)) {
            updatingItem.setAvailable(itemDto.getAvailable());
            log.info("Обновлено поле available item с id {}", itemId);
        }
        itemStorage.update(updatingItem);
        return ItemMapper.toItemDto(updatingItem);
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
