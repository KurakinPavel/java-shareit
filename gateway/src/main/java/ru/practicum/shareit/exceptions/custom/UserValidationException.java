package ru.practicum.shareit.exceptions.custom;

public class UserValidationException extends RuntimeException {
    public UserValidationException(final String message) {
        super(message);
    }
}
