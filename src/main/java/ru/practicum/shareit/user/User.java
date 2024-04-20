package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class User {
     protected int id;
     @NotBlank
     @Size(min = 1, max = 100)
     protected String name;
     @NotBlank
     @Email
     protected String email;
}
