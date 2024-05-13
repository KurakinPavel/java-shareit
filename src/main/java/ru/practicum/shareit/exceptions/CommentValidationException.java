package ru.practicum.shareit.exceptions;

public class CommentValidationException extends RuntimeException {
    public CommentValidationException(final String message) {
        super(message);
    }
}
