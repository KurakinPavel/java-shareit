package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDtoOut {
    protected int id;
    protected String comment;
    protected String authorName;
    protected LocalDateTime created;
}
