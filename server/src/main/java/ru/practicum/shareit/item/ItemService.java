package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDtoForItemInformation;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.exceptions.custom.CommentValidationException;
import ru.practicum.shareit.exceptions.custom.ItemValidationException;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
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
    private final UserRepository userStorage;
    private final BookingRepository bookingStorage;
    private final ItemRequestRepository itemRequestStorage;

    @Transactional
    public ItemDto add(int ownerId, ItemDto itemDto) {
        if ((itemDto.getName() == null) || (itemDto.getName().isBlank()) || itemDto.getDescription() == null
                || itemDto.getDescription().isBlank() || itemDto.getAvailable() == null) {
            throw new ItemValidationException("Переданы некорректные данные для создания item");
        }
        User owner = userStorage.getReferenceById(ownerId);
        UserMapper.toUserDto(owner);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() > 0) {
            itemRequest = itemRequestStorage.getReferenceById(itemDto.getRequestId());
            ItemRequestMapper.toItemRequestDtoOut(itemRequest);
        }
        Item item = ItemMapper.toItem(itemDto, owner, itemRequest);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Transactional
    public CommentDtoOut addComment(int userId, int itemId, CommentDtoIn commentDtoIn) {
        List<Booking> lastBookingByItemAndUser = bookingStorage.findFirst1ByItemIdAndBookerIdAndEndIsBeforeOrderByEndDesc(
                itemId, userId, LocalDateTime.now());
        if (lastBookingByItemAndUser.isEmpty()) {
            throw new CommentValidationException("Данные об аренде item с id " + itemId +
                    " пользователем с id " + userId + " не найдены. Добавление комментария отклонено.");
        }
        Item item = itemStorage.getReferenceById(itemId);
        User author = userStorage.getReferenceById(userId);
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
            List<Booking> lastBookingByItem = bookingStorage.findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(
                    item.getId(), LocalDateTime.now());
            if (!lastBookingByItem.isEmpty()) {
                bookings.add(lastBookingByItem.get(0));
            } else {
                return ItemMapper.toItemDtoWithBookingInformation(item, null, null, comments);
            }
            List<Booking> nextBookingByItem = bookingStorage.findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(
                    item.getId(), LocalDateTime.now());
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
    public List<ItemDtoWithBookingInformation> getItemsOfOwner(int ownerId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Item> itemsOfOwner = itemStorage.findAllByOwnerIdOrderById(ownerId, pageable);
        List<ItemDtoWithBookingInformation> itemsWithBookingInformation = new ArrayList<>();
        for (Item item : itemsOfOwner) {
            List<CommentDtoOut> comments = commentStorage.findAllByItemId(item.getId())
                    .stream()
                    .map(CommentMapper::toCommentDtoOut)
                    .collect(Collectors.toList());
            List<Booking> bookings = new ArrayList<>();
            List<Booking> lastBookingByItem = bookingStorage.findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(
                    item.getId(), LocalDateTime.now());
            if (!lastBookingByItem.isEmpty()) {
                bookings.add(lastBookingByItem.get(0));
            } else {
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item, null, null, comments);
                itemsWithBookingInformation.add(itemDtoWithBookingInformation);
                continue;
            }
            List<Booking> nextBookingByItem = bookingStorage.findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(
                    item.getId(), LocalDateTime.now());
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
    public List<ItemDto> getItemsForRent(String text, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.search(text, pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
