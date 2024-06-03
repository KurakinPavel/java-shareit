package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    protected Integer id;
    protected String name;
    protected String description;
    protected Boolean available;
    protected Integer requestId;

    public ItemDto(Integer id, String name, String description, Boolean available, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
