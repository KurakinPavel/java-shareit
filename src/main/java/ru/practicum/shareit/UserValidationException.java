package ru.practicum.shareit;

public class UserValidationException extends RuntimeException {
    public UserValidationException(final String message) {
        super(message);
    }
}
