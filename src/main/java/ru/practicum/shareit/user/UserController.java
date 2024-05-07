package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public UserDto save(@Valid @RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Integer userId,
                       @RequestBody UserDto userDto) {
        return userService.update(userId, userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable Integer userId) {
        userService.remove(userId);
    }
}
