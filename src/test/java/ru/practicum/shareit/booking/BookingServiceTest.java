package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingDtoForIn;
import ru.practicum.shareit.booking.model.BookingDtoForOut;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceTest {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void add() {
        BookingDtoForIn bookingDtoForIn = new BookingDtoForIn(1, LocalDateTime.of(2024, 6, 15, 10, 0),
                LocalDateTime.of(2024, 6, 16, 10, 0));
        UserDto userDto1 = new UserDto(0, "Andrey", "andrey@mail.ru");
        UserDto savedOwner = userService.save(userDto1);
        UserDto userDto2 = new UserDto(0, "Vladimir", "vladimir@mail.ru");
        UserDto savedBooker = userService.save(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая", Boolean.valueOf("true"), 0);
        ItemDto savedItem = itemService.add(savedOwner.getId(), itemDto);
        BookingDtoForOut bookingDtoForOut = bookingService.add(savedBooker.getId(), bookingDtoForIn);
        Assertions.assertEquals(1, bookingDtoForOut.getId(), "Id сохранённого бронирования не соответствует ожидаемому");
    }

    @Test
    void confirm() {
        BookingDtoForIn bookingDtoForIn = new BookingDtoForIn(1, LocalDateTime.of(2024, 6, 15, 10, 0),
                LocalDateTime.of(2024, 6, 16, 10, 0));
        UserDto userDto1 = new UserDto(0, "Andrey", "andrey@mail.ru");
        UserDto savedOwner = userService.save(userDto1);
        UserDto userDto2 = new UserDto(0, "Vladimir", "vladimir@mail.ru");
        UserDto savedBooker = userService.save(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая", Boolean.valueOf("true"), 0);
        ItemDto savedItem = itemService.add(savedOwner.getId(), itemDto);
        BookingDtoForOut bookingDtoForOut = bookingService.add(savedBooker.getId(), bookingDtoForIn);
        Assertions.assertEquals(BookingStatus.WAITING, bookingDtoForOut.getStatus(), "Статус нового бронирования не WAITING");
        BookingDtoForOut bookingDtoForOutAPPROVED = bookingService.confirm(savedOwner.getId(), bookingDtoForOut.getId(), Boolean.valueOf("true"));
        Assertions.assertEquals(BookingStatus.APPROVED, bookingDtoForOutAPPROVED.getStatus(), "Статус подтверждённого бронирования не APPROVED");
    }

    @Test
    void getBooking() {
        BookingDtoForIn bookingDtoForIn = new BookingDtoForIn(1, LocalDateTime.of(2024, 6, 15, 10, 0),
                LocalDateTime.of(2024, 6, 16, 10, 0));
        UserDto userDto1 = new UserDto(0, "Andrey", "andrey@mail.ru");
        UserDto savedOwner = userService.save(userDto1);
        UserDto userDto2 = new UserDto(0, "Vladimir", "vladimir@mail.ru");
        UserDto savedBooker = userService.save(userDto2);
        ItemDto itemDto = new ItemDto(0, "Отвёртка", "Отвёртка электрическая", Boolean.valueOf("true"), 0);
        ItemDto savedItem = itemService.add(savedOwner.getId(), itemDto);
        BookingDtoForOut bookingDtoForOut = bookingService.add(savedBooker.getId(), bookingDtoForIn);
        BookingDtoForOut receivedBooking = bookingService.getBooking(savedOwner.getId(), bookingDtoForOut.getId());
        Assertions.assertEquals(receivedBooking.getId(), bookingDtoForOut.getId(), "Сохранённое и подтверждённое бронирования не совпадают");
    }
}
