package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

//    @PostMapping
//    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
//                    @RequestBody ItemDto itemDto) {
//        return itemService.add(ownerId, itemDto);
//    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                       @PathVariable Integer itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Integer itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsOfOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemService.getItemsOfOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsForRent(@RequestParam String text) {
        return itemService.getItemsForRent(text);
    }
}
