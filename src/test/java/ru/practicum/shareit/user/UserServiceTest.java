package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {
    private final UserService service;

    @Test
    void findAll() {
        UserDto userDto1 = new UserDto(0, "Andrey", "andrey@mail.ru");
        service.save(userDto1);
        UserDto userDto2 = new UserDto(0, "Vladimir", "vladimir@mail.ru");
        service.save(userDto2);
        List<UserDto> savedUsers = service.findAll();
        Assertions.assertEquals(2, savedUsers.size(), "Количество сохранённых пользователей не соответствует ожидаемому");
    }

    @Test
    void save() {
        UserDto userDto = new UserDto(0, "Andrey", "andrey@mail.ru");
        UserDto savedUser = service.save(userDto);
        Assertions.assertEquals(1, savedUser.getId(), "Id сохранённого пользователя не соответствует ожидаемому");
        Assertions.assertEquals("andrey@mail.ru", savedUser.getEmail(), "Email сохранённого " +
                "пользователя не соответствует ожидаемому");
    }

    @Test
    void update() {
        UserDto userDto = new UserDto(0, "Andrey", "andrey@mail.ru");
        UserDto savedUser = service.save(userDto);
        UserDto updateForSavedUser = new UserDto(0, "AndreyPavlovich", "andreyp@mail.ru");
        UserDto updatedUser = service.update(savedUser.getId(), updateForSavedUser);
        Assertions.assertEquals(savedUser.getId(), updatedUser.getId(), "Id обновлённого пользователя не соответствует ожидаемому");
        Assertions.assertEquals("andreyp@mail.ru", updatedUser.getEmail(), "Email обновлённого " +
                "пользователя не соответствует ожидаемому");
    }

    @Test
    void getUserById() {
        UserDto userDto = new UserDto(0, "Andrey", "andrey@mail.ru");
        UserDto savedUser = service.save(userDto);
        UserDto receivedUser = service.getUserById(savedUser.getId());
        Assertions.assertEquals(savedUser.getId(), receivedUser.getId(), "Id полученного пользователя не соответствует ожидаемому");
        Assertions.assertEquals(savedUser.getEmail(), receivedUser.getEmail(), "Email полученного " +
                "пользователя не соответствует ожидаемому");
    }

    @Test
    void remove() {
        UserDto userDto1 = new UserDto(0, "Andrey", "andrey@mail.ru");
        UserDto savedUser1 = service.save(userDto1);
        UserDto userDto2 = new UserDto(0, "Vladimir", "vladimir@mail.ru");
        UserDto savedUser2 = service.save(userDto2);
        List<UserDto> savedUsers1 = service.findAll();
        Assertions.assertEquals(2, savedUsers1.size(), "Количество сохранённых пользователей не соответствует ожидаемому");
        service.remove(savedUser1.getId());
        List<UserDto> savedUsers2 = service.findAll();
        Assertions.assertEquals(1, savedUsers2.size(), "Количество сохранённых пользователей не соответствует ожидаемому");
        Assertions.assertEquals(savedUser2.getId(), savedUsers2.get(0).getId(), "Id сохранённого пользователя не соответствует ожидаемому");
    }
}