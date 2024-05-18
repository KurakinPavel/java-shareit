package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOut add(@RequestHeader("X-Sharer-User-Id") Integer requesterId,
                                 @RequestBody @Valid ItemRequestDtoIn itemRequestDtoIn) {
        return itemRequestService.add(requesterId, itemRequestDtoIn);
    }

    @GetMapping
    public List<ItemRequestDtoOutWithItemsInformation> getItemRequestsOfItemRequester(
            @RequestHeader("X-Sharer-User-Id") Integer itemRequesterId) {
        return itemRequestService.getItemRequestsOfItemRequester(itemRequesterId);
    }

}
