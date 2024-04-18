package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    protected int id;
    protected String name;
    protected String description;
    protected Boolean available;
    protected int requestId;
}
