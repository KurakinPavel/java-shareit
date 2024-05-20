package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.custom.PaginationParamsValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.ItemDtoForItemRequestOut;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequestDtoOutWithItemsInformation;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestStorage;
    private final UserService userService;
    private final ItemRepository itemStorage;

    public ItemRequestDtoOut add(int itemRequesterId, ItemRequestDtoIn itemRequestDtoIn) {
        User itemRequester = userService.getUserForInternalUse(itemRequesterId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDtoIn, itemRequester);
        return ItemRequestMapper.toItemRequestDtoOut(itemRequestStorage.save(itemRequest));
    }

    public List<ItemRequestDtoOutWithItemsInformation> getAllItemRequestsWithPagination(int itemRequesterId, int from, int size) {
        User itemRequester = userService.getUserForInternalUse(itemRequesterId);
        if (from < 0) {
            throw new PaginationParamsValidationException("Индекс первого элемента не может быть меньше нуля");
        }
        if (size < 1) {
            throw new PaginationParamsValidationException("Количество отображаемых элементов не может быть меньше одного");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        Page<ItemRequest> itemRequestsOfItemRequester = itemRequestStorage.findAllByItemRequester_IdNotOrderByCreatedDesc(itemRequesterId, pageable);
        List<ItemRequestDtoOutWithItemsInformation> itemRequestsWithItemsInformation = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestsOfItemRequester) {
            List<ItemDtoForItemRequestOut> items = itemStorage.findAllByItemRequest_Id(itemRequest.getId())
                    .stream()
                    .map(ItemMapper::toItemDtoForItemRequestOut)
                    .collect(Collectors.toList());
            ItemRequestDtoOutWithItemsInformation itemRequestDtoOutWithItemsInformation =
                    ItemRequestMapper.toItemRequestDtoOutWithItemsInformation(itemRequest, items);
            itemRequestsWithItemsInformation.add(itemRequestDtoOutWithItemsInformation);
        }
        return itemRequestsWithItemsInformation;
    }

    public List<ItemRequestDtoOutWithItemsInformation> getItemRequestsOfItemRequester(int itemRequesterId) {
        User itemRequester = userService.getUserForInternalUse(itemRequesterId);
        List<ItemRequest> itemRequestsOfItemRequester = itemRequestStorage.findAllByItemRequester_IdOrderByCreatedDesc(itemRequesterId);
        List<ItemRequestDtoOutWithItemsInformation> itemRequestsWithItemsInformation = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestsOfItemRequester) {
            List<ItemDtoForItemRequestOut> items = itemStorage.findAllByItemRequest_Id(itemRequest.getId())
                    .stream()
                    .map(ItemMapper::toItemDtoForItemRequestOut)
                    .collect(Collectors.toList());
            ItemRequestDtoOutWithItemsInformation itemRequestDtoOutWithItemsInformation =
                    ItemRequestMapper.toItemRequestDtoOutWithItemsInformation(itemRequest, items);
            itemRequestsWithItemsInformation.add(itemRequestDtoOutWithItemsInformation);
        }
        return itemRequestsWithItemsInformation;
    }

    public ItemRequestDtoOutWithItemsInformation getItemRequestById(int itemRequesterId, int itemRequestId) {
        User itemRequester = userService.getUserForInternalUse(itemRequesterId);
        ItemRequest itemRequest = getItemRequestForInternalUse(itemRequestId);
        List<ItemDtoForItemRequestOut> items = itemStorage.findAllByItemRequest_Id(itemRequestId)
                .stream()
                .map(ItemMapper::toItemDtoForItemRequestOut)
                .collect(Collectors.toList());
        return ItemRequestMapper.toItemRequestDtoOutWithItemsInformation(itemRequest, items);
    }

    public ItemRequest getItemRequestForInternalUse(int id) {
        ItemRequest itemRequest = itemRequestStorage.getReferenceById(id);
        ItemRequestMapper.toItemRequestDtoOut(itemRequest);
        return itemRequest;
    }
}
