package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequestOut;
import ru.practicum.shareit.user.User;
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

    public ItemRequest getItemRequestForInternalUse(int id) {
        ItemRequest itemRequest = itemRequestStorage.getReferenceById(id);
        ItemRequestMapper.toItemRequestDtoOut(itemRequest);
        return itemRequest;
    }
}
