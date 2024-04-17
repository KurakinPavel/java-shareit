package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
     protected int id;
     @NotBlank
     @Size(min = 1, max = 100)
     protected String name;
     @NotBlank
     @Email
     protected String email;
}
