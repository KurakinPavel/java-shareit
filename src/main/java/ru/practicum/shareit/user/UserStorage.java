package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> findAll();

    User getUser(int id);

    User create(User user);

    User update(int userId, UserDto userDto);

    Map<String, String> remove(int userId);

}
