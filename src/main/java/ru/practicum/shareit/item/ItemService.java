package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDtoForItemInformation;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
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
        if (item.getOwner().getId() != userId) {
            return ItemMapper.toItemDtoWithBookingInformation(item, null, null);
        } else {
            List<Booking> bookings = new ArrayList<>();
            List<Booking> lastBooking = bookingStorage.findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(item.getId(), LocalDateTime.now());
            if (!lastBooking.isEmpty()) {
                bookings.add(lastBooking.get(0));
            }
            List<Booking> nextBooking = bookingStorage.findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), LocalDateTime.now());
            if (!nextBooking.isEmpty()) {
                bookings.add(nextBooking.get(0));
            }
            if (bookings.isEmpty()) {
                return ItemMapper.toItemDtoWithBookingInformation(item, null, null);
            } else if (bookings.size() == 1) {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
                return ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(0));
            } else {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
               return ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(1));
            }
        }
    }

    public List<ItemDtoWithBookingInformation> getItemsOfOwner(int ownerId) {
        List<Item> itemsOfOwner = itemStorage.findAllByOwnerId(ownerId);
        List<ItemDtoWithBookingInformation> itemsWithBookingInformation = new ArrayList<>();
        for (Item item : itemsOfOwner) {
            List<Booking> bookings = new ArrayList<>();
            List<Booking> lastBooking = bookingStorage.findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(item.getId(), LocalDateTime.now());
            if (!lastBooking.isEmpty()) {
                bookings.add(lastBooking.get(0));
            }
            List<Booking> nextBooking = bookingStorage.findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), LocalDateTime.now());
            if (!nextBooking.isEmpty()) {
                bookings.add(nextBooking.get(0));
            }
            if (bookings.isEmpty()) {
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item, null, null);
                itemsWithBookingInformation.add(itemDtoWithBookingInformation);
            } else if (bookings.size() == 1) {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(0));
                itemsWithBookingInformation.add(itemDtoWithBookingInformation);
            } else {
                List<BookingDtoForItemInformation> bookingsForItemInformation =
                        bookings
                                .stream()
                                .map(BookingMapper::toBookingDtoForItemInformation)
                                .collect(Collectors.toList());
                ItemDtoWithBookingInformation itemDtoWithBookingInformation =
                        ItemMapper.toItemDtoWithBookingInformation(item,
                                bookingsForItemInformation.get(0), bookingsForItemInformation.get(1));
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
