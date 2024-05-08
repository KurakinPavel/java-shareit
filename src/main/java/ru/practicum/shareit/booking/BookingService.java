package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BookingValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
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
public class BookingService {
    private final BookingRepository bookingStorage;
    private final UserRepository userStorage;
    private final ItemRepository itemStorage;

    public BookingDtoForOut add(int bookerId, BookingDtoForIn bookingDtoForIn) {
        if (bookingDtoForIn.getStart() == null || bookingDtoForIn.getEnd() == null || bookingDtoForIn.getStart().isAfter(bookingDtoForIn.getEnd())
                || bookingDtoForIn.getStart().equals(bookingDtoForIn.getEnd()))
            throw new BookingValidationException("Переданы некорректные данные для создания бронирования");
        User booker = userStorage.getReferenceById(bookerId);
        try {
            String bookerEmail = booker.getEmail();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Пользователь с id " + bookerId + " не найден.");
        }
        Item item = itemStorage.getReferenceById(bookingDtoForIn.getItemId());
        try {
            Boolean available = item.getAvailable();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Item с id " + bookingDtoForIn.getItemId() + " не найден.");
        }
        if (item.getOwner().getId() == bookerId) throw new NoSuchElementException("Владелец не может бронировать свои вещи");
        if (!item.getAvailable()) throw new BookingValidationException("Переданы некорректные данные для создания бронирования");
        Booking booking = BookingMapper.toBooking(bookingDtoForIn, booker, item);
        return BookingMapper.toBookingDtoForOut(bookingStorage.save(booking));
    }

    public BookingDtoForOut confirm(int ownerId, int bookingId, Boolean approved) {
        Booking booking = bookingStorage.getReferenceById(bookingId);
        if (booking.getItem().getOwner().getId() != ownerId)
            throw new NoSuchElementException("Подтверждать бронирование может только владелец вещи");
        if (booking.getStatus().equals(BookingStatus.APPROVED)) throw new BookingValidationException("Бронирование уже подтверждено");
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDtoForOut(bookingStorage.save(booking));
    }

    public BookingDtoForOut getBooking(int userId, int bookingId) {
        User booker = userStorage.getReferenceById(userId);
        try {
            String bookerEmail = booker.getEmail();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден.");
        }
        Booking booking = bookingStorage.getReferenceById(bookingId);
        if (!(booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId))
            throw new NoSuchElementException("У пользователя нет ни вещей, ни бронирований");
        return BookingMapper.toBookingDtoForOut(booking);
    }

    public List<BookingDtoForOut> getBookings(int userId, String state) {
        User booker = userStorage.getReferenceById(userId);
        try {
            String bookerEmail = booker.getEmail();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Пользователь с id " + userId + " не найден.");
        }
        List<Booking> bookings;
        if (!isPresent(state)) throw new IllegalArgumentException("Unknown state: " + state);
        switch (BookingState.valueOf(state)) {
            case ALL:
                bookings = bookingStorage.findAllByBooker_IdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingStorage.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingStorage.findAllByBooker_IdAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingStorage.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingStorage.findAllByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(state));
                break;
            default:
                return new ArrayList<>();
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForOut)
                .collect(Collectors.toList());
    }

    public List<BookingDtoForOut> getBookingsOfOwnerItems(int ownerId, String state) {
        User owner = userStorage.getReferenceById(ownerId);
        try {
            String bookerEmail = owner.getEmail();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Пользователь с id " + ownerId + " не найден.");
        }
        List<Booking> bookings;
        if (!isPresent(state)) throw new IllegalArgumentException("Unknown state: " + state);
        switch (BookingState.valueOf(state)) {
            case ALL:
                bookings = bookingStorage.findAllByItem_Owner_IdOrderByStartDesc(ownerId);
                break;
            case CURRENT:
                bookings = bookingStorage.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingStorage.findAllByItem_Owner_IdAndEndIsAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingStorage.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingStorage.findAllByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, BookingStatus.valueOf(state));
                break;
            default:
                return new ArrayList<>();
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoForOut)
                .collect(Collectors.toList());
    }

    private static boolean isPresent(String data) {
        try {
            Enum.valueOf(BookingState.class, data);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
