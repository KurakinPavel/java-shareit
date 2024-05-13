package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.UserValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userStorage;

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        if ((userDto.getName() == null) || (userDto.getName().isBlank()) || userDto.getEmail() == null
                || userDto.getEmail().isBlank()) {
            throw new UserValidationException("Переданы некорректные данные для создания user");
        }
        return UserMapper.toUserDto(userStorage.save(UserMapper.toUser(userDto)));
    }

    @Transactional
    public UserDto update(int userId, UserDto userDto) {
        User updatingUser = userStorage.getReferenceById(userId);
        if (userDto.getEmail() != null && !(userDto.getEmail().isBlank())) {
            updatingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !(userDto.getName().isBlank())) {
            updatingUser.setName(userDto.getName());
        }
        userStorage.save(updatingUser);
        log.info("Обновлены данные user с id {}", userId);
        return UserMapper.toUserDto(userStorage.getReferenceById(updatingUser.getId()));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userStorage.getReferenceById(id));
    }

    public User getUserForInternalUse(int id) {
        User user = userStorage.getReferenceById(id);
        UserMapper.toUserDto(user);
        return user;
    }

    @Transactional
    public void remove(int userId) {
        userStorage.deleteById(userId);
    }
}
