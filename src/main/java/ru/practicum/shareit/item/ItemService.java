package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDtoForItemInformation;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.CommentValidationException;
import ru.practicum.shareit.exceptions.ItemValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
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
    private final UserRepository userStorage;
    private final BookingRepository bookingStorage;
    private final CommentRepository commentStorage;

    public ItemDto add(int ownerId, ItemDto itemDto) {
        if ((itemDto.getName() == null) || (itemDto.getName().isBlank()) || itemDto.getDescription() == null
                || itemDto.getDescription().isBlank() || itemDto.getAvailable() == null)
            throw new ItemValidationException("Переданы некорректные данные для создания item");
        User owner = userStorage.getReferenceById(ownerId);
        try {
            String ownerEmail = owner.getEmail();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Пользователь с id " + ownerId + " не найден.");
        }
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    public CommentDtoOut addComment(int userId, int itemId, CommentDtoIn commentDtoIn) {
        List<Booking> lastBooking = bookingStorage.findFirst1ByItemIdAndBookerIdAndEndIsBeforeOrderByEndDesc(
                itemId, userId, LocalDateTime.now());
        if (lastBooking.isEmpty()) throw new CommentValidationException("Данные об аренде item с id " + itemId +
                " пользователем с id " + userId + " не найдены. Добавление комментария отклонено.");
        Item item = itemStorage.getReferenceById(itemId);
        User author = userStorage.getReferenceById(userId);
        Comment comment = CommentMapper.toComment(commentDtoIn, item, author);
        return CommentMapper.toCommentDtoOut(commentStorage.save(comment));
    }

    public ItemDto update(int ownerId, int itemId, ItemDto itemDto) {
        Item updatingItem = itemStorage.getReferenceById(itemId);
        if (updatingItem.getOwner().getId() != ownerId)
            throw new NoSuchElementException("Редактировать данные item может только владелец");
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
            List<Booking> lastBooking = bookingStorage.findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(item.getId(),
                    LocalDateTime.now());
            if (!lastBooking.isEmpty()) {
                bookings.add(lastBooking.get(0));
            }
            List<Booking> nextBooking = bookingStorage.findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(item.getId(),
                    LocalDateTime.now());
            if (!nextBooking.isEmpty()) {
                bookings.add(nextBooking.get(0));
            }
            if (bookings.isEmpty()) {
                return ItemMapper.toItemDtoWithBookingInformation(item, null, null, comments);
            } else if (bookings.size() == 1) {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
                return ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(0), comments);
            } else {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
               return ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(1), comments);
            }
        }
    }

    public List<ItemDtoWithBookingInformation> getItemsOfOwner(int ownerId) {
        List<Item> itemsOfOwner = itemStorage.findAllByOwnerId(ownerId);
        List<ItemDtoWithBookingInformation> itemsWithBookingInformation = new ArrayList<>();
        for (Item item : itemsOfOwner) {
            List<CommentDtoOut> comments = commentStorage.findAllByItemId(item.getId())
                    .stream()
                    .map(CommentMapper::toCommentDtoOut)
                    .collect(Collectors.toList());
            List<Booking> bookings = new ArrayList<>();
            List<Booking> lastBooking = bookingStorage.findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(item.getId(),
                    LocalDateTime.now());
            if (!lastBooking.isEmpty()) {
                bookings.add(lastBooking.get(0));
            }
            List<Booking> nextBooking = bookingStorage.findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(item.getId(),
                    LocalDateTime.now());
            if (!nextBooking.isEmpty()) {
                bookings.add(nextBooking.get(0));
            }
            if (bookings.isEmpty()) {
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item, null, null, comments);
                itemsWithBookingInformation.add(itemDtoWithBookingInformation);
            } else if (bookings.size() == 1) {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(0), comments);
                itemsWithBookingInformation.add(itemDtoWithBookingInformation);
            } else {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(1), comments);
                itemsWithBookingInformation.add(itemDtoWithBookingInformation);
            }
        }
        return itemsWithBookingInformation;
    }

    public List<ItemDto> getItemsForRent(String text) {
        if (text.isEmpty()) return new ArrayList<>();
        return itemStorage.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
