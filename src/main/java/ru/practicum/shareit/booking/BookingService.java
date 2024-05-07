package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BookingValidationException;
import ru.practicum.shareit.exceptions.ItemValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@Slf4j
@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingStorage;
    private final UserRepository userStorage;
    private final ItemRepository itemStorage;

    public Booking add(int bookerId, BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().equals(bookingDto.getEnd()))
            throw new BookingValidationException("Переданы некорректные данные для создания бронирования");
        User booker = userStorage.getReferenceById(bookerId);
        try {
            String bookerEmail = booker.getEmail();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Пользователь с id " + bookerId + " не найден.");
        }
        Item item = itemStorage.getReferenceById(bookingDto.getItemId());
        try {
            Boolean available = item.getAvailable();
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Item с id " + bookingDto.getItemId() + " не найден.");
        }
        if (!item.getAvailable()) throw new BookingValidationException("Переданы некорректные данные для создания бронирования");
        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        return bookingStorage.save(booking);
    }
}
