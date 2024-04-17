package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    protected int id;
    protected LocalDateTime start;
    protected LocalDateTime end;
    protected int item;
    protected int booker;
    protected BookingStatus status;
}
