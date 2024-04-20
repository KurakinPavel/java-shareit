package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequest {
    protected int id;
    @NotBlank
    @Size(min = 1, max = 500)
    protected String description;
    @Positive
    protected int requestor;
    protected LocalDateTime created;
}
