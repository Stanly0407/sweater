package com.sv.sweater.service;

import com.sv.sweater.domain.Role;
import com.sv.sweater.domain.User;
import com.sv.sweater.repositories.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;


@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
// заглушки
    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void addUser() {
        User user = new User();

        user.setEmail("some@mail.ru");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertTrue(isUserCreated); // Тест будет проверять то, что пользователь успешно создан
        Assert.assertNotNull(user.getActivationCode()); //проверяем задан ли активационный код
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER))); //проверяем, задана ли роль
        // Проверка сохранен ли пользователь и что ему выполнена отправка сообщения
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.eq("Activation code"),
                        ArgumentMatchers.contains("Welcome to Sweater.")
                        //  ArgumentMatchers.anyString() ----можно и так
                );
    }
    // но м.б. случаи, когда юзер в БД уже имеется => создаем тест

    @Test
    public void addUserFailTest(){
        User user = new User();

        user.setUsername("Kate");

        //Эмулируем, что такой юзер в БД есть
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Kate");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated); // то есть здесь вернулось false, когда мы попытались добавить юзера, кот. есть в БД

        // Проверка, что у нас не отправляет метод никаких сообщений и не происходит сохранение этого юзера в БД
        // Проверка сохранен ли пользователь и что ему выполнена отправка сообщения
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    void activateUser() {
        User user = new User();

        user.setActivationCode("some activation code");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());
        // проверим сохраняется ли юзер
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }



}