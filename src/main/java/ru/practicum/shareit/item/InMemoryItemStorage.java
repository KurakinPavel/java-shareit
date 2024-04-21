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

    @Override
    public List<Item> getItemsOfOwner(int ownerId) {
        List<Item> itemsOfOwner = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId() == ownerId) itemsOfOwner.add(item);
        }
        return itemsOfOwner;
    }

    @Override
    public List<Item> getItemsForRent(String text) {
        List<Item> itemsForRent = new ArrayList<>();
        if (text.isEmpty()) return new ArrayList<>();
        String textInLowercase = text.toLowerCase();
        for (Item item : items.values()) {
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
    public Item add(Item item) {
        item.setId(++numerator);
        items.put(numerator, item);
        return getItem(numerator);
    }

    @Override
    public void update(Item item) {
        items.put(item.getId(), item);
    }
}
