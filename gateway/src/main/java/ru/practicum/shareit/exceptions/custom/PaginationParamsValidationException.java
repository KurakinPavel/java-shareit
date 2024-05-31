package ru.practicum.shareit.exceptions.custom;

public class PaginationParamsValidationException extends RuntimeException {
    public PaginationParamsValidationException(final String message) {
        super(message);
    }
}
