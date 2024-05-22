package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDtoForIn;
import ru.practicum.shareit.booking.model.BookingDtoForOut;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoForOut add(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                @Valid @RequestBody BookingDtoForIn bookingDtoForIn) {
        return bookingService.add(bookerId, bookingDtoForIn);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoForOut confirm(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @PathVariable Integer bookingId,
            @RequestParam("approved") Boolean approved) {
        return bookingService.confirm(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoForOut getBooking(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoForOut> getBookings(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "20") Integer size) {
        return bookingService.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoForOut> getBookingsOfOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "20") Integer size) {
        return bookingService.getBookingsOfOwnerItems(ownerId, state, from, size);
    }
}
