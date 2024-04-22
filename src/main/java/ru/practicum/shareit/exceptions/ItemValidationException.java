package ru.practicum.shareit.exceptions;

public class ItemValidationException extends RuntimeException {
    public ItemValidationException(final String message) {
        super(message);
    }
}
