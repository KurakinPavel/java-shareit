package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.ItemDtoForItemRequestOut;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDtoOutWithItemsInformation {
    protected Integer id;
    protected String description;
    protected LocalDateTime created;
    protected List<ItemDtoForItemRequestOut> items;
}
