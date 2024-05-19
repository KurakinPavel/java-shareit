package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoForIn;
import ru.practicum.shareit.booking.dto.BookingDtoForOut;
import ru.practicum.shareit.exceptions.custom.BookingValidationException;
import ru.practicum.shareit.exceptions.custom.PaginationParamsValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingStorage;
    private final ItemRepository itemStorage;
    private final UserService userService;

    @Transactional
    public BookingDtoForOut add(int bookerId, BookingDtoForIn bookingDtoForIn) {
        if (bookingDtoForIn.getStart() == null || bookingDtoForIn.getEnd() == null || bookingDtoForIn.getStart().isAfter(bookingDtoForIn.getEnd())
                || bookingDtoForIn.getStart().equals(bookingDtoForIn.getEnd())) {
            throw new BookingValidationException("Переданы некорректные данные для создания бронирования");
        }
        User booker = userService.getUserForInternalUse(bookerId);
        Item item = getItemForInternalUse(bookingDtoForIn.getItemId());
        if (item.getOwner().getId() == bookerId) {
            throw new NoSuchElementException("Владелец не может бронировать свои вещи");
        }
        if (!item.getAvailable()) {
            throw new BookingValidationException("Переданы некорректные данные для создания бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingDtoForIn, booker, item);
        return BookingMapper.toBookingDtoForOut(bookingStorage.save(booking));
    }

    @Transactional
    public BookingDtoForOut confirm(int ownerId, int bookingId, Boolean approved) {
        Booking booking = bookingStorage.getReferenceById(bookingId);
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new NoSuchElementException("Подтверждать бронирование может только владелец вещи");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingValidationException("Бронирование уже подтверждено");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDtoForOut(bookingStorage.save(booking));
    }

    @Transactional(readOnly = true)
    public BookingDtoForOut getBooking(int userId, int bookingId) {
        User user = userService.getUserForInternalUse(userId);
        Booking booking = bookingStorage.getReferenceById(bookingId);
        if (!(booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)) {
            throw new NoSuchElementException("У пользователя нет ни вещей, ни бронирований");
        }
        return BookingMapper.toBookingDtoForOut(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingDtoForOut> getBookings(int bookerId, String state, Integer from, int size) {
        User user = userService.getUserForInternalUse(bookerId);
        if (from < 0) {
            throw new PaginationParamsValidationException("Индекс первого элемента не может быть меньше нуля");
        }
        if (size < 1) {
            throw new PaginationParamsValidationException("Количество отображаемых элементов не может быть меньше одного");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Booking> bookings;
        if (isPresent(state)) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        switch (BookingState.valueOf(state)) {
            case ALL:
                bookings = bookingStorage.findAllByBooker_IdOrderByStartDesc(bookerId, pageable);
                break;
            case CURRENT:
                bookings = bookingStorage.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookings = bookingStorage.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = bookingStorage.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingStorage.findAllByBooker_IdAndStatusOrderByStartDesc(bookerId, BookingStatus.valueOf(state), pageable);
                break;
            default:
                return new ArrayList<>();
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForOut)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDtoForOut> getBookingsOfOwnerItems(int ownerId, String state, int from, int size) {
        User owner = userService.getUserForInternalUse(ownerId);
        if (from < 0) {
            throw new PaginationParamsValidationException("Индекс первого элемента не может быть меньше нуля");
        }
        if (size < 1) {
            throw new PaginationParamsValidationException("Количество отображаемых элементов не может быть меньше одного");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Booking> bookings;
        if (isPresent(state)) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        switch (BookingState.valueOf(state)) {
            case ALL:
                bookings = bookingStorage.findAllByItem_Owner_IdOrderByStartDesc(ownerId, pageable);
                break;
            case CURRENT:
                bookings = bookingStorage.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId,
                        LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookings = bookingStorage.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = bookingStorage.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingStorage.findAllByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.valueOf(state), pageable);
                break;
            default:
                return new ArrayList<>();
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForOut)
                .collect(Collectors.toList());
    }

    public List<Booking> getLastBookingByItemAndUser(int itemId, int userId) {
        return bookingStorage.findFirst1ByItemIdAndBookerIdAndEndIsBeforeOrderByEndDesc(itemId, userId, LocalDateTime.now());
    }

    public List<Booking> getLastBookingByItem(int itemId) {
        return bookingStorage.findFirst1ByItemIdAndStartIsBeforeOrderByStartDesc(itemId, LocalDateTime.now());
    }

    public List<Booking> getNextBookingByItem(int itemId) {
        return bookingStorage.findFirst1ByItemIdAndStartIsAfterOrderByStartAsc(itemId, LocalDateTime.now());
    }

    private Item getItemForInternalUse(int id) {
        Item item = itemStorage.getReferenceById(id);
        ItemMapper.toItemDto(item);
        return item;
    }

    private static boolean isPresent(String data) {
        try {
            Enum.valueOf(BookingState.class, data);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
