package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    protected int numerator;
    protected final Map<Integer, User> users;
    protected final Map<String, Integer> userEmails;

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
    public User getUser(int id) {
        User user = users.get(id);
        if (user != null) return user;
        log.info("Пользователь с идентификатором {} не найден.", id);
        throw new NoSuchElementException("Пользователь с id " + id + " не найден.");
    }

    @Override
    public User create(User user) {
        if (userEmails.containsKey(user.getEmail())) throw new IllegalArgumentException("Пользователь с email " +
                user.getEmail() + " уже существует. Добавление отклонено.");
        user.setId(++numerator);
        users.put(numerator, user);
        userEmails.put(user.getEmail(), numerator);
        log.info("Добавлен новый пользователь с идентификатором {}", numerator);
        return users.get(numerator);
    }

    @Override
    public User update(int userId, User user) {
        User updatingUser = getUser(userId);
        String updatingEmail = updatingUser.getEmail();
        if (userEmails.containsKey(user.getEmail()) && userEmails.get(user.getEmail()) != userId)
            throw new IllegalArgumentException("Пользователь с аналогичным email " +
                user.getEmail() + " уже существует. Обновление отклонено.");
        if (user.getEmail() != null && !(user.getEmail().isBlank())) {
            userEmails.remove(updatingEmail);
            updatingUser.setEmail(user.getEmail());
            userEmails.put(user.getEmail(), userId);
            log.info("Обновлен email пользователя с id {}", userId);
        }
        if (user.getName() != null && !(user.getName().isBlank())) {
            updatingUser.setName(user.getName());
            log.info("Обновлено name пользователя с id {}", userId);
        }
        users.put(userId, updatingUser);
        return users.get(userId);
    }

    @Override
    public Map<String, String> remove(int userId) {
        User removingUser = users.remove(userId);
        if (removingUser == null) throw new NoSuchElementException("Пользователь с id " + userId + " не найден. " +
                "Удаление отклонено.");
        userEmails.remove(removingUser.getEmail());
        log.info("Удалён пользователь с id {}", userId);
        return Map.of("result", "Удалён пользователь с id " + userId);
    }
}
