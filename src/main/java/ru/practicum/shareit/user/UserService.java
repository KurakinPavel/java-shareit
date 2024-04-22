package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserValidationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto create(UserDto userDto) {
        if ((userDto.getName() == null) || (userDto.getName().isBlank()) || userDto.getEmail() == null
                || userDto.getEmail().isBlank())
            throw new UserValidationException("Переданы некорректные данные для создания user");
        User userByEmail = userStorage.getUserByEmail(userDto.getEmail());
        if (userByEmail != null) throw new IllegalArgumentException("Пользователь с email " +
                userDto.getEmail() + " уже существует. Добавление отклонено.");
        return UserMapper.toUserDto(userStorage.create(UserMapper.toUser(userDto)));
    }

    public UserDto update(int userId, UserDto userDto) {
        User updatingUser = userStorage.getUserById(userId);
        User userByEmail = userStorage.getUserByEmail(userDto.getEmail());
        if (userByEmail != null && !updatingUser.equals(userByEmail)) {
            throw new IllegalArgumentException("Пользователь с аналогичным email " +
                    userByEmail.getEmail() + " уже существует. Обновление отклонено.");
        }
        String updatingEmail = updatingUser.getEmail();
        if (userDto.getEmail() != null && !(userDto.getEmail().isBlank())) {
            updatingUser.setEmail(userDto.getEmail());
            log.info("Обновлено поле email user с id {}", userId);
        }
        if (userDto.getName() != null && !(userDto.getName().isBlank())) {
            updatingUser.setName(userDto.getName());
            log.info("Обновлено поле name user с id {}", userId);
        }
        userStorage.update(updatingEmail, updatingUser);
        return UserMapper.toUserDto(updatingUser);
    }

    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    public Map<String, String> remove(int userId) {
        return userStorage.remove(userId);
    }
}
