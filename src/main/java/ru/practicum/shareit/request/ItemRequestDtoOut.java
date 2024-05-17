package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDtoOut {
    protected int id;
    protected String description;
    protected LocalDateTime created;
}
