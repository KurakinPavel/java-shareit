package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDtoOut {
    protected int id;
    protected String text;
    protected String authorName;
    protected LocalDateTime created;
}
