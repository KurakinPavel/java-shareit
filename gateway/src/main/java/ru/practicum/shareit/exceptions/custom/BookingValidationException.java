package ru.practicum.shareit.exceptions.custom;

public class BookingValidationException extends RuntimeException {
    public BookingValidationException(final String message) {
        super(message);
    }
}
