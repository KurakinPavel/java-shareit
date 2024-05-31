package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemDtoForItemRequestOut {
    protected Integer id;
    protected String name;
    protected String description;
    protected Integer requestId;
    protected Boolean available;
}
