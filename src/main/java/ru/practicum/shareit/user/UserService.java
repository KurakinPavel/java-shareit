package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(int userId, UserDto userDto) {
        return userStorage.update(userId, userDto);
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public Map<String, String> remove(int userId) {
        return userStorage.remove(userId);
    }

}
