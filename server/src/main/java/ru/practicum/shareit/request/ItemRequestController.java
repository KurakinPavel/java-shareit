package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequestDtoOutWithItemsInformation;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOut add(@RequestHeader("X-Sharer-User-Id") Integer requesterId,
                                 @RequestBody ItemRequestDtoIn itemRequestDtoIn) {
        return itemRequestService.add(requesterId, itemRequestDtoIn);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOutWithItemsInformation> getItemRequestsWithPagination(
            @RequestHeader("X-Sharer-User-Id") Integer itemRequesterId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "20") Integer size) {
        return itemRequestService.getAllItemRequestsWithPagination(itemRequesterId, from, size);
    }

    @GetMapping
    public List<ItemRequestDtoOutWithItemsInformation> getItemRequestsOfItemRequester(
            @RequestHeader("X-Sharer-User-Id") Integer itemRequesterId) {
        return itemRequestService.getItemRequestsOfItemRequester(itemRequesterId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOutWithItemsInformation getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Integer itemRequesterId,
            @PathVariable Integer requestId) {
        return itemRequestService.getItemRequestById(itemRequesterId, requestId);
    }
}
