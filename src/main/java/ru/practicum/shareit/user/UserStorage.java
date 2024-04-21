package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> findAll();

    User getUserById(int id);

    User getUserByEmail(String email);

    User create(User user);

    void update(String email, User user);

    Map<String, String> remove(int userId);

}
