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
        if (users.containsKey(id)) return users.get(id);
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
    public User update(int userId, UserDto userDto) {
        User updatingUser = getUser(userId);
        String updatingEmail = updatingUser.getEmail();
        if (userEmails.containsKey(userDto.getEmail()) && userEmails.get(userDto.getEmail()) != userId)
            throw new IllegalArgumentException("Пользователь с аналогичным email " +
                userDto.getEmail() + " уже существует. Обновление отклонено.");
        if (userDto.getEmail() != null) {
            userEmails.remove(updatingEmail);
            updatingUser.setEmail(userDto.getEmail());
            userEmails.put(userDto.getEmail(), userId);
            log.info("Обновлен email пользователя с id {}", userId);
        }
        if (userDto.getName() != null) {
            updatingUser.setName(userDto.getName());
            log.info("Обновлено name пользователя с id {}", userId);
        }
        users.put(userId, updatingUser);
        return users.get(userId);
    }

    @Override
    public Map<String, String> remove(int userId) {
        User removingUser = getUser(userId);
        users.remove(userId);
        userEmails.remove(removingUser.getEmail());
        log.info("Удалён пользователь с id {}", userId);
        return Map.of("result", "Удалён пользователь с id " + userId);
    }
}
