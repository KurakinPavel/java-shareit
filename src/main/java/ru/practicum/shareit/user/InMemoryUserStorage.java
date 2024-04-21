package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    protected int numerator;
    protected final Map<String, User> users;
    protected final Map<Integer, String> userEmails;

    public InMemoryUserStorage() {
        numerator = 0;
        users = new HashMap<>();
        userEmails = new HashMap<>();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        User user = users.get(userEmails.get(id));
        if (user != null) return user;
        log.info("Пользователь с идентификатором {} не найден.", id);
        throw new NoSuchElementException("Пользователь с id " + id + " не найден.");
    }

    @Override
    public User getUserByEmail(String email) {
        return users.get(email);
    }

    @Override
    public User create(User user) {
        user.setId(++numerator);
        users.put(user.getEmail(), user);
        userEmails.put(numerator, user.getEmail());
        log.info("Добавлен новый пользователь с идентификатором {}", numerator);
        return users.get(userEmails.get(numerator));
    }

    @Override
    public void update(String email, User user) {
        userEmails.remove(user.getId());
        userEmails.put(user.getId(), user.getEmail());
        users.remove(email);
        users.put(user.getEmail(), user);
    }

    @Override
    public Map<String, String> remove(int userId) {
        User removingUser = users.remove(userEmails.get(userId));
        if (removingUser == null) throw new NoSuchElementException("Пользователь с id " + userId + " не найден. " +
                "Удаление отклонено.");
        userEmails.remove(removingUser.getId());
        log.info("Удалён пользователь с id {}", userId);
        return Map.of("result", "Удалён пользователь с id " + userId);
    }
}
