package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    protected int id;
    @NotBlank
    @Size(min = 1, max = 100)
    protected String name;
    @NotBlank
    @Size(min = 1, max = 500)
    protected String description;
    @NotBlank
    protected Boolean available;
    @Positive
    protected int owner;
    protected ItemRequest request;

}
