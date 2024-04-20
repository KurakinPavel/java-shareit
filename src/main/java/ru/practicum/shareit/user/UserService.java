package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            throw new NullPointerException("Переданы некорректные данные для создания user");
        return UserMapper.toUserDto(userStorage.create(UserMapper.toUser(userDto)));
    }

    public UserDto update(int userId, UserDto userDto) {

        return UserMapper.toUserDto(userStorage.update(userId, UserMapper.toUser(userDto)));
    }

    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userStorage.getUser(id));
    }

    public Map<String, String> remove(int userId) {
        return userStorage.remove(userId);
    }
}
