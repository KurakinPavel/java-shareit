package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                    @RequestBody ItemDto itemDto) {
        return itemService.add(ownerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                             @PathVariable Integer itemId,
                             @Valid @RequestBody CommentDtoIn commentDtoIn) {
        return itemService.addComment(userId, itemId, commentDtoIn);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                       @PathVariable Integer itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingInformation getItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookingInformation> getItemsOfOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemService.getItemsOfOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsForRent(@RequestParam String text) {
        return itemService.getItemsForRent(text);
    }

}
