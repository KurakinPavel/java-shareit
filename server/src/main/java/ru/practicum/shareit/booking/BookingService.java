package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.exceptions.custom.BookingValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
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
public class BookingService {
    private final BookingRepository bookingStorage;
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;

    @Transactional
    public BookingDtoForOut add(int bookerId, BookingDtoForIn bookingDtoForIn) {
        User booker = userStorage.getReferenceById(bookerId);
        UserMapper.toUserDto(booker);
        Item item = itemStorage.getReferenceById(bookingDtoForIn.getItemId());
        ItemMapper.toItemDto(item);
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
        User user = userStorage.getReferenceById(userId);
        UserMapper.toUserDto(user);
        Booking booking = bookingStorage.getReferenceById(bookingId);
        if (!(booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)) {
            throw new NoSuchElementException("У пользователя нет ни вещей, ни бронирований");
        }
        return BookingMapper.toBookingDtoForOut(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingDtoForOut> getBookings(int bookerId, String state, int from, int size) {
        User user = userStorage.getReferenceById(bookerId);
        UserMapper.toUserDto(user);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Booking> bookings;
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
        User owner = userStorage.getReferenceById(ownerId);
        UserMapper.toUserDto(owner);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Booking> bookings;
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
}
