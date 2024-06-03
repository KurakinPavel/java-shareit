package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    protected Integer id;
    protected String name;
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
