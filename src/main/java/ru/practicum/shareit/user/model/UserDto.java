package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto {
    protected Integer id;
    @NotNull
    @NotBlank
    protected String name;
    @NotNull
    @NotBlank
    @Email
    protected String email;

    public UserDto(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDto() {
    }
}
