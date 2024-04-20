package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

@Getter
@Setter
public class Item {
    protected int id;
    protected String name;
    protected String description;
    protected Boolean available;
    protected int owner;
    protected ItemRequest request;

    public Item(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
