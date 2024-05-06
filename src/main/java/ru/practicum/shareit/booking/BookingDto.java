package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingDto {
    protected Integer id;
    @NotNull
    @NotBlank
    @Future
    protected LocalDateTime start;
    @NotNull
    @NotBlank
    @Future
    protected LocalDateTime end;
    @NotNull
    @NotBlank
    protected BookingStatus status;
    @NotNull
    @NotBlank
    protected Integer bookerId;
    @NotNull
    @NotBlank
    protected Integer itemId;
    @NotNull
    @NotBlank
    protected String itemName;
}
