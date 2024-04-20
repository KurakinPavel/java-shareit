package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    protected Integer id;
    @NotNull
    @NotBlank
    protected String name;
    @NotNull
    @NotBlank
    @Email
    protected String email;
}
