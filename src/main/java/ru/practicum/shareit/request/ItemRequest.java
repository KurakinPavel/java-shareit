package ru.practicum.shareit.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    protected int id;
    @NotBlank
    @Size(min = 1, max = 500)
    protected String description;
    @Positive
    protected int requestor;
    protected LocalDateTime created;
}
