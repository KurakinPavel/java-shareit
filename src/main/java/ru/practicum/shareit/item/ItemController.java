package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") @Positive Integer ownerId,
                    @RequestBody ItemDto itemDto) throws NoSuchFieldException {
        return itemService.add(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") @Positive Integer ownerId,
                       @PathVariable Integer itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemDto(@PathVariable Integer itemId) {
        return itemService.getItemDto(itemId);
    }

    @GetMapping
    public List<Item> getItemsOfOwner(@RequestHeader("X-Sharer-User-Id") @Positive Integer ownerId) {
        return itemService.getItemsOfOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsForRent(@RequestParam String text) {
        return itemService.getItemsForRent(text);
    }
}
