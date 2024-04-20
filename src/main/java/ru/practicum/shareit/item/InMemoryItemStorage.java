package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.*;

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {
    @Autowired
    protected final UserStorage userStorage;
    protected int numerator;
    protected final Map<Integer, Item> items;

    public InMemoryItemStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
        numerator = 0;
        items = new HashMap<>();
    }

    private List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItemsOfOwner(int ownerId) {
        List<Item> itemsOfOwner = new ArrayList<>();
        for (Item item : findAll()) {
            if (item.getOwner() == ownerId) itemsOfOwner.add(item);
        }
        return itemsOfOwner;
    }

    @Override
    public List<ItemDto> getItemsForRent(String text) {
        List<ItemDto> itemsForRent = new ArrayList<>();
        if (text.isEmpty()) return new ArrayList<>();
        String textInLowercase = text.toLowerCase();
        for (Item item : findAll()) {
            if ((item.getName().toLowerCase().contains(textInLowercase) ||
                    item.getDescription().toLowerCase().contains(textInLowercase)) && item.getAvailable()) {
                itemsForRent.add(ItemMapper.toItemDto(item));
            }
        }
        return itemsForRent;
    }

    private Item getItem(int id) {
        if (items.containsKey(id)) return items.get(id);
        log.info("Item с идентификатором {} не найден.", id);
        throw new NoSuchElementException("Item с id " + id + " не найден.");
    }

    @Override
    public ItemDto getItemDto(int itemId) {
        return ItemMapper.toItemDto(getItem(itemId));
    }

    @Override
    public Item add(int ownerId, ItemDto itemDto) throws NoSuchFieldException {
        User owner = userStorage.getUser(ownerId);
        if ((itemDto.getName() == null) || (itemDto.getName().isBlank()) || itemDto.getDescription() == null
                || itemDto.getDescription().isBlank() || itemDto.getAvailable() == null)
            throw new NoSuchFieldException("Переданы некорректные данные для создания item");
        Item item = new Item(
                ++numerator,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
        item.setOwner(ownerId);
        items.put(numerator, item);
        return getItem(numerator);
    }

    @Override
    public Item update(int ownerId, int itemId, ItemDto itemDto) {
        User owner = userStorage.getUser(ownerId);
        Item updatingItem = getItem(itemId);
        if (updatingItem.getOwner() != ownerId)
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
        items.put(itemId, updatingItem);
        return getItem(itemId);
    }
}
