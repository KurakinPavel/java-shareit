package ru.practicum.shareit.exceptions.custom;

public class CommentValidationException extends RuntimeException {
    public CommentValidationException(final String message) {
        super(message);
    }
}
