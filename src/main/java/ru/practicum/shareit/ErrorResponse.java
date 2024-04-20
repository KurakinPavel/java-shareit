package ru.practicum.shareit;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;
    private final Throwable throwable;

    public ErrorResponse(String error, Throwable throwable) {
        this.error = error;
        this.throwable = throwable;
    }
}
