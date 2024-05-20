package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemDto {
    protected Integer id;
    @NotNull
    @NotBlank
    protected String name;
    @NotNull
    @NotBlank
    protected String description;
    @NotNull
    @NotBlank
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
