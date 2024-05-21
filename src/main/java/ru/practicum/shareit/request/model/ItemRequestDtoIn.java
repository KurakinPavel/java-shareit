package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDtoIn {
    protected Integer id;
    @NotBlank
    @Size(min = 20)
    protected String description;
}
