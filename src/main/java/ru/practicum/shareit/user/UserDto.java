package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    protected Integer id;
    // Тесты сформированы так, что в случае обновления "на входе" в любом поле может быть null значение.
    // Соответствующую аннотацию использовать не могу.
    protected String name;
    @Email
    protected String email;
}
