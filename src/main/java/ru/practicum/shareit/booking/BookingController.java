package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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



}
