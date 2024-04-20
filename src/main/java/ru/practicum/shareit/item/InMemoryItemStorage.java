package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {
    protected int numerator;
    protected final Map<Integer, Item> items;

    public InMemoryItemStorage() {
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
    public List<Item> getItemsForRent(String text) {
        List<Item> itemsForRent = new ArrayList<>();
        if (text.isEmpty()) return new ArrayList<>();
        String textInLowercase = text.toLowerCase();
        for (Item item : findAll()) {
            if ((item.getName().toLowerCase().contains(textInLowercase) ||
                    item.getDescription().toLowerCase().contains(textInLowercase)) && item.getAvailable()) {
                itemsForRent.add(item);
            }
        }
        return itemsForRent;
    }

    @Override
    public Item getItem(int id) {
        if (items.containsKey(id)) return items.get(id);
        log.info("Item с идентификатором {} не найден.", id);
        throw new NoSuchElementException("Item с id " + id + " не найден.");
    }

    @Override
    public Item add(int ownerId, Item item) {
        item.setId(++numerator);
        item.setOwner(ownerId);
        items.put(numerator, item);
        return getItem(numerator);
    }

    @Override
    public Item update(int ownerId, int itemId, ItemDto itemDto) {
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
