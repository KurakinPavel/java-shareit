package ru.practicum.shareit.exceptions;

public class BookingValidationException extends RuntimeException {
    public BookingValidationException(final String message) {
        super(message);
    }
}
