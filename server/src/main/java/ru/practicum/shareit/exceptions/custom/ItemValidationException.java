package ru.practicum.shareit.exceptions.custom;

public class ItemValidationException extends RuntimeException {
    public ItemValidationException(final String message) {
        super(message);
    }
}
