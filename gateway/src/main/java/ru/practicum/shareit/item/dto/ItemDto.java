package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    protected Integer id;
    @NotNull
    @NotBlank
    protected String name;
    @NotNull
    @NotBlank
    protected String description;
    @NotNull
    protected Boolean available;
    protected Integer requestId;
}
