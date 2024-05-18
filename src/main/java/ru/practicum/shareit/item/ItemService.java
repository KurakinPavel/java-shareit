package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoForItemInformation;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.exceptions.custom.CommentValidationException;
import ru.practicum.shareit.exceptions.custom.ItemValidationException;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingInformation;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemStorage;
    private final CommentRepository commentStorage;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;

    @Transactional
    public ItemDto add(int ownerId, ItemDto itemDto) {
        if ((itemDto.getName() == null) || (itemDto.getName().isBlank()) || itemDto.getDescription() == null
                || itemDto.getDescription().isBlank() || itemDto.getAvailable() == null) {
            throw new ItemValidationException("Переданы некорректные данные для создания item");
        }
        User owner = userService.getUserForInternalUse(ownerId);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() > 0) {
            itemRequest = itemRequestService.getItemRequestForInternalUse(itemDto.getRequestId());
        }
        Item item = ItemMapper.toItem(itemDto, owner, itemRequest);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Transactional
    public CommentDtoOut addComment(int userId, int itemId, CommentDtoIn commentDtoIn) {
        List<Booking> lastBookingByItemAndUser = bookingService.getLastBookingByItemAndUser(itemId, userId);
        if (lastBookingByItemAndUser.isEmpty()) {
            throw new CommentValidationException("Данные об аренде item с id " + itemId +
                    " пользователем с id " + userId + " не найдены. Добавление комментария отклонено.");
        }
        Item item = itemStorage.getReferenceById(itemId);
        User author = userService.getUserForInternalUse(userId);
        Comment comment = CommentMapper.toComment(commentDtoIn, item, author);
        return CommentMapper.toCommentDtoOut(commentStorage.save(comment));
    }

    @Transactional
    public ItemDto update(int ownerId, int itemId, ItemDto itemDto) {
        Item updatingItem = itemStorage.getReferenceById(itemId);
        if (updatingItem.getOwner().getId() != ownerId) {
            throw new NoSuchElementException("Редактировать данные item может только владелец");
        }
        if (!(itemDto.getName() == null || itemDto.getName().isBlank())) {
            updatingItem.setName(itemDto.getName());
        }
        if (!(itemDto.getDescription() == null || itemDto.getDescription().isBlank())) {
            updatingItem.setDescription(itemDto.getDescription());
        }
        if (!(itemDto.getAvailable() == null)) {
            updatingItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemStorage.save(updatingItem));
    }

    @Transactional(readOnly = true)
    public ItemDtoWithBookingInformation getItem(int userId, int itemId) {
        Item item = itemStorage.getReferenceById(itemId);
        List<CommentDtoOut> comments = commentStorage.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(Collectors.toList());
        if (item.getOwner().getId() != userId) {
            return ItemMapper.toItemDtoWithBookingInformation(item, null, null, comments);
        } else {
            List<Booking> bookings = new ArrayList<>();
            List<Booking> lastBookingByItem = bookingService.getLastBookingByItem(item.getId());
            if (!lastBookingByItem.isEmpty()) {
                bookings.add(lastBookingByItem.get(0));
            } else {
                return ItemMapper.toItemDtoWithBookingInformation(item, null, null, comments);
            }
            List<Booking> nextBookingByItem = bookingService.getNextBookingByItem(item.getId());
            if (!nextBookingByItem.isEmpty()) {
                bookings.add(nextBookingByItem.get(0));
            }
            List<BookingDtoForItemInformation> bookingsForItemInformation =
                    bookings.stream()
                            .map(BookingMapper::toBookingDtoForItemInformation)
                            .collect(Collectors.toList());
            if (bookings.size() == 1) {
                return ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), null, comments);
            } else {
                return ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(1), comments);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ItemDtoWithBookingInformation> getItemsOfOwner(int ownerId) {
        List<Item> itemsOfOwner = itemStorage.findAllByOwnerId(ownerId);
        List<ItemDtoWithBookingInformation> itemsWithBookingInformation = new ArrayList<>();
        for (Item item : itemsOfOwner) {
            List<CommentDtoOut> comments = commentStorage.findAllByItemId(item.getId())
                    .stream()
                    .map(CommentMapper::toCommentDtoOut)
                    .collect(Collectors.toList());
            List<Booking> bookings = new ArrayList<>();
            List<Booking> lastBookingByItem = bookingService.getLastBookingByItem(item.getId());
            if (!lastBookingByItem.isEmpty()) {
                bookings.add(lastBookingByItem.get(0));
            } else {
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item, null, null, comments);
                itemsWithBookingInformation.add(itemDtoWithBookingInformation);
                continue;
            }
            List<Booking> nextBookingByItem = bookingService.getNextBookingByItem(item.getId());
            if (!nextBookingByItem.isEmpty()) {
                bookings.add(nextBookingByItem.get(0));
            }
            List<BookingDtoForItemInformation> bookingsForItemInformation =
                    bookings.stream()
                            .map(BookingMapper::toBookingDtoForItemInformation)
                            .collect(Collectors.toList());
            ItemDtoWithBookingInformation itemDtoWithBookingInformation;
            if (bookings.size() == 1) {
                itemDtoWithBookingInformation = ItemMapper.toItemDtoWithBookingInformation(item,
                        bookingsForItemInformation.get(0), null, comments);
            } else {
                itemDtoWithBookingInformation = ItemMapper.toItemDtoWithBookingInformation(item,
                        bookingsForItemInformation.get(0), bookingsForItemInformation.get(1), comments);
            }
            itemsWithBookingInformation.add(itemDtoWithBookingInformation);
        }
        return itemsWithBookingInformation;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getItemsForRent(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
