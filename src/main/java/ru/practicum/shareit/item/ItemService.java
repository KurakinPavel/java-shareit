package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;

    public ItemDto add(int ownerId, ItemDto itemDto) {
        if ((itemDto.getName() == null) || (itemDto.getName().isBlank()) || itemDto.getDescription() == null
                || itemDto.getDescription().isBlank() || itemDto.getAvailable() == null)
            throw new ItemValidationException("Переданы некорректные данные для создания item");
        User owner = userStorage.getReferenceById(ownerId);
        try {
            String ownerEmail = owner.getEmail();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Пользователь с id " + ownerId + " не найден.");
        }
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    public ItemDto update(int ownerId, int itemId, ItemDto itemDto) {
        Item updatingItem = itemStorage.getReferenceById(itemId);
        if (updatingItem.getOwner().getId() != ownerId)
            throw new NoSuchElementException("Редактировать данные item может только владелец");
        if (!(itemDto.getName() == null || itemDto.getName().isBlank())) {
            updatingItem.setName(itemDto.getName());
        }
        if (!(itemDto.getDescription() == null || itemDto.getDescription().isBlank())) {
            updatingItem.setDescription(itemDto.getDescription());
        }
        if (!(itemDto.getAvailable() == null)) {
            updatingItem.setAvailable(itemDto.getAvailable());
        }
        itemStorage.save(updatingItem);
        return ItemMapper.toItemDto(itemStorage.getReferenceById(updatingItem.getId()));
    }

    public ItemDto getItem(int itemId) {
        return ItemMapper.toItemDto(itemStorage.getReferenceById(itemId));
    }

    public List<ItemDto> getItemsOfOwner(int ownerId) {
        return itemStorage.findAllByOwnerId(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getItemsForRent(String text) {
        if (text.isEmpty()) return new ArrayList<>();
        return itemStorage.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
