package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Item {
    protected int id;
    @NotBlank
    @Size(min = 2, max = 100)
    protected String name;
    @NotBlank
    @Size(min = 2, max = 500)
    protected String description;
    @NotNull
    protected Boolean available;
    @Positive
    protected int owner;
    protected ItemRequest request;

    public Item(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
