package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Integer requesterId,
                                      @RequestBody @Valid ItemRequestDtoIn itemRequestDtoIn) {
        return itemRequestClient.add(requesterId, itemRequestDtoIn);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsWithPagination(
            @RequestHeader("X-Sharer-User-Id") Integer itemRequesterId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "20") Integer size) {
        return itemRequestClient.getAllItemRequestsWithPagination(itemRequesterId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsOfItemRequester(
            @RequestHeader("X-Sharer-User-Id") Integer itemRequesterId) {
        return itemRequestClient.getItemRequestsOfItemRequester(itemRequesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Integer itemRequesterId,
            @PathVariable Integer requestId) {
        return itemRequestClient.getItemRequestById(itemRequesterId, requestId);
    }

}
